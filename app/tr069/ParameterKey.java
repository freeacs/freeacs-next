package tr069;

import dbi.JobParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr069.http.HTTPRequestResponseData;
import tr069.xml.ParameterValueStruct;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;

public class ParameterKey {
  private static final Logger log = LoggerFactory.getLogger(ParameterKey.class);

  private String cpeKey;
  private String serverKey;

  public void setServerKey(HTTPRequestResponseData reqRes) throws NoSuchAlgorithmException {
    this.serverKey = calculateParameterKey(reqRes);
  }

  public boolean isEqual() {
    return cpeKey != null && cpeKey.equals(serverKey);
  }

  private static String calculateParameterKey(HTTPRequestResponseData reqRes)
      throws NoSuchAlgorithmException {
    SessionData sessionData = reqRes.getSessionData();
    dbi.UnittypeParameters utps = sessionData.getUnittype().getUnittypeParameters();
    Map<String, ParameterValueStruct> fromDB = sessionData.getFromDB();
    String jobId = sessionData.getAcsParameters().getValue(dbi.util.SystemParameters.JOB_CURRENT);
    if (jobId != null && !"".equals(jobId.trim())) {
      dbi.Job job = sessionData.getUnittype().getJobs().getById(Integer.valueOf(jobId));
      if (job != null) {
        log.debug("Current job has jobId: "
                + job.getId()
                + " -> verification stage, must retrieve job parameters (with RW-flag) to calculate parameterkey correctly");
        Map<String, JobParameter> jobParams = job.getDefaultParameters();
        for (Entry<String, JobParameter> jobParamEntry : jobParams.entrySet()) {
          if (jobParamEntry
              .getValue()
              .getParameter()
              .getUnittypeParameter()
              .getFlag()
              .isReadWrite()) {
            ParameterValueStruct jobParamPvs =
                new ParameterValueStruct(
                    jobParamEntry.getKey(), jobParamEntry.getValue().getParameter().getValue());
            fromDB.put(jobParamEntry.getKey(), jobParamPvs);
          }
        }
      }
    }
    StringBuilder valuesBuilder = new StringBuilder();
    for (Entry<String, ParameterValueStruct> entry : fromDB.entrySet()) {
      String utpName = entry.getKey();
      //			ParameterInfoStruct pis = infoMap.get(utpName);
      dbi.UnittypeParameter utp = utps.getByName(utpName);
      //			if (pis != null && pis.isWritable()) {
      if (utp != null && utp.getFlag().isReadWrite()) {
        if (utpName.contains("PeriodicInformInterval")
            || "ExtraCPEParam".equals(entry.getValue().getValue())) {
          continue;
        }
        valuesBuilder.append(entry.getValue().getValue());
      }
    }
    String values = valuesBuilder.toString();
    if ("".equals(values)) {
      log.debug("No device parameter values found, ACS parameterkey = \"No data in DB\"");
      return "No data in DB";
    } else {
      MessageDigest md = MessageDigest.getInstance("SHA");
      byte[] hash = md.digest(values.getBytes());
      StringBuilder parameterKey = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(b + 128);
        if (hex.length() == 1) {
          hex = "0" + hex;
        }
        parameterKey.append(hex);
      }
      String pk = parameterKey.substring(0, 32);
      log.debug("The values to be hashed: " + values + " -> ACS parameterkey = " + pk);
      return pk;
    }
  }

    public String getCpeKey() {
        return cpeKey;
    }

    public void setCpeKey(String cpeKey) {
        this.cpeKey = cpeKey;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }
}
