package tr069;

import dbi.JobParameter;
import dbi.Unittype.ProvisioningProtocol;
import tr069.base.ACSParameters;
import tr069.base.PIIDecision;
import tr069.base.SessionDataI;
import tr069.http.HTTPRequestResponseData;
import tr069.xml.ParameterList;
import tr069.xml.ParameterValueStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionData implements SessionDataI {
  private static final Logger log = LoggerFactory.getLogger(SessionData.class);

  private final String id;
  private final List<HTTPRequestResponseData> reqResList = new ArrayList<>();
  private Long startupTmsForSession;

  private String unitId;
  private dbi.Unit unit;
  private dbi.Profile profile;
  private dbi.Unittype unittype;
  private String keyRoot;

  private String serialNumber;

  private boolean periodic;
  /* other event codes */
  private boolean factoryReset;
  private boolean valueChange;
  private boolean kicked;
  private boolean transferComplete;
  private boolean autonomousTransferComplete;
  private boolean diagnosticsComplete;
  private boolean booted;

  private boolean jobUnderExecution;
  private String eventCodes;

  private ACSParameters acsParameters;
  private CPEParameters cpeParameters;
  private InformParameters informParameters;

  private Map<String, ParameterValueStruct> fromDB;
  private List<ParameterValueStruct> valuesFromCPE;
  private ParameterList toCPE;
  private List<ParameterValueStruct> toDB;
  private List<ParameterValueStruct> requestedCPE;

  private dbi.Job job;
  private Map<String, JobParameter> jobParams;

  private ParameterKey parameterKey;
  private CommandKey commandKey;
  private boolean provisioningAllowed = true;

  private String secret;
  private boolean firstConnect;
  private boolean unittypeCreated = true;

  private PIIDecision piiDecision;

  private dbi.util.ProvisioningMessage provisioningMessage = new dbi.util.ProvisioningMessage();

  private Download download;

  private String cwmpVersionNumber;

  public SessionData(String id) {
    this.id = id;
    provisioningMessage.setProvProtocol(ProvisioningProtocol.TR069);
  }

  public void setKeyRoot(String keyRoot) {
    if (keyRoot != null) {
      this.keyRoot = keyRoot;
    }
  }

  public void setUnitId(String unitId) {
    if (unitId != null) {
      this.unitId = unitId;
      this.provisioningMessage.setUniqueId(unitId);
    }
  }

  public void setNoMoreRequests(boolean noMoreRequests) {
    log.warn("Setting unused noMoreRequests field to " + noMoreRequests);
  }

  public void setToDB(List<ParameterValueStruct> toDB) {
    if (toDB == null) {
      toDB = new ArrayList<>();
    }
    this.toDB = toDB;
  }

  public String getMethodBeforePreviousResponseMethod() {
    if (reqResList.size() > 2) {
      return reqResList.get(reqResList.size() - 3).getResponseData().getMethod();
    } else {
      return null;
    }
  }

  public String getPreviousResponseMethod() {
    if (reqResList.size() > 1) {
      return reqResList.get(reqResList.size() - 2).getResponseData().getMethod();
    } else {
      return null;
    }
  }

  public void setEventCodes(String eventCodes) {
    this.eventCodes = eventCodes;
    this.provisioningMessage.setEventCodes(eventCodes);
  }

  public String getSoftwareVersion() {
    if (cpeParameters != null) {
      return cpeParameters.getValue(cpeParameters.SOFTWARE_VERSION);
    }
    return null;
  }

  public void setSoftwareVersion(String softwareVersion) {
    if (cpeParameters != null) {
      cpeParameters.getCpeParams().put(cpeParameters.SOFTWARE_VERSION, new ParameterValueStruct(cpeParameters.SOFTWARE_VERSION, softwareVersion));
    }
  }

  public boolean lastProvisioningOK() {
    return parameterKey.isEqual() && commandKey.isEqual();
  }

  @Override
  public PIIDecision getPIIDecision() {
    if (piiDecision == null) {
      piiDecision = new PIIDecision(this);
    }
    return piiDecision;
  }

  public boolean discoverUnittype() {
    if (acsParameters != null
        && acsParameters.getValue(dbi.util.SystemParameters.DISCOVER) != null
        && "1".equals(acsParameters.getValue(dbi.util.SystemParameters.DISCOVER))) {
      return true;
    } else if (acsParameters == null) {
      log.debug("freeacsParameters not found in discoverUnittype()");
    } else if (acsParameters.getValue(dbi.util.SystemParameters.DISCOVER) != null) {
      log.debug("DISCOVER parameter value is "
              + acsParameters.getValue(dbi.util.SystemParameters.DISCOVER)
              + " in discoverUnittype()");
    } else {
      log.debug("DISCOVER parameter not found of value is null in discoverUnittype() ");
    }
    return false;
  }

  String getUnittypeName() {
    String unittypeName = null;
    if (unittype != null) {
      unittypeName = unittype.getName();
    }
    return unittypeName;
  }

  public String getVersion() {
    String version = null;
    if (cpeParameters != null) {
      version = cpeParameters.getValue(cpeParameters.SOFTWARE_VERSION);
    }
    return version;
  }

  /** The session-id. */
  public String getId() {
    return id;
  }

  /** The unique id for the CPE. */
  @Override
  public String getUnitId() {
    return unitId;
  }

  /** The unit-object. */
  @Override
  public dbi.Unit getUnit() {
    return unit;
  }

  @Override
  public void setUnit(dbi.Unit unit) {
    this.unit = unit;
  }

  /** The profile name for this CPE (defined i the DB). */
  @Override
  public dbi.Profile getProfile() {
    return profile;
  }

  @Override
  public void setProfile(dbi.Profile profile) {
    this.profile = profile;
  }

  /** The unittype for this CPE (defined in the DB). */
  @Override
  public dbi.Unittype getUnittype() {
    return unittype;
  }

  @Override
  public void setUnittype(dbi.Unittype unittype) {
    this.unittype = unittype;
  }

  /** The keyroot of this CPE (e.g. InternetGatewayDevice.) */
  public String getKeyRoot() {
    return keyRoot;
  }

  @Override
  public String getSerialNumber() {
    return serialNumber;
  }

  @Override
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  /** Tells whether the CPE is doing a periodic inform or not. */
  public boolean isPeriodic() {
    return periodic;
  }

  public void setPeriodic(boolean periodic) {
    this.periodic = periodic;
  }

  public boolean isFactoryReset() {
    return factoryReset;
  }

  public void setFactoryReset(boolean factoryReset) {
    this.factoryReset = factoryReset;
  }

  public boolean isValueChange() {
    return valueChange;
  }

  public void setValueChange(boolean valueChange) {
    this.valueChange = valueChange;
  }

  public boolean isKicked() {
    return kicked;
  }

  public void setKicked(boolean kicked) {
    this.kicked = kicked;
  }

  public boolean isTransferComplete() {
    return transferComplete;
  }

  public void setTransferComplete(boolean transferComplete) {
    this.transferComplete = transferComplete;
  }

  public boolean isAutonomousTransferComplete() {
    return autonomousTransferComplete;
  }

  public void setAutonomousTransferComplete(boolean autonomousTransferComplete) {
    this.autonomousTransferComplete = autonomousTransferComplete;
  }

  public boolean isDiagnosticsComplete() {
    return diagnosticsComplete;
  }

  public void setDiagnosticsComplete(boolean diagnosticsComplete) {
    this.diagnosticsComplete = diagnosticsComplete;
  }

  public boolean isBooted() {
    return booted;
  }

  public void setBooted(boolean booted) {
    this.booted = booted;
  }

  /** Tells whether a job is under execution - important not to start on another job. */
  public boolean isJobUnderExecution() {
    return jobUnderExecution;
  }

  public void setJobUnderExecution(boolean jobUnderExecution) {
    this.jobUnderExecution = jobUnderExecution;
  }

  /** The event code of the inform. */
  public String getEventCodes() {
    return eventCodes;
  }

  /** Owera parameters. */
  @Override
  public ACSParameters getAcsParameters() {
    return acsParameters;
  }

  @Override
  public void setAcsParameters(ACSParameters acsParameters) {
    this.acsParameters = acsParameters;
  }

  /** Special parameters, will always be retrieved. */
  public CPEParameters getCpeParameters() {
    return cpeParameters;
  }

  public void setCpeParameters(CPEParameters cpeParameters) {
    this.cpeParameters = cpeParameters;
  }

  /** Special parameter, will only be retrieved from the Inform. */
  public InformParameters getInformParameters() {
    return informParameters;
  }

  public void setInformParameters(InformParameters informParameters) {
    this.informParameters = informParameters;
  }

  /** All parameters found in the DB, except system parameters (X). */
  @Override
  public Map<String, ParameterValueStruct> getFromDB() {
    return fromDB;
  }

  @Override
  public void setFromDB(Map<String, ParameterValueStruct> fromDB) {
    this.fromDB = fromDB;
  }

  /** All parameters read from the CPE. */
  public List<ParameterValueStruct> getValuesFromCPE() {
    return valuesFromCPE;
  }

  public void setValuesFromCPE(List<ParameterValueStruct> valuesFromCPE) {
    this.valuesFromCPE = valuesFromCPE;
  }

  /** All parameters that shall be written to the CPE. */
  public ParameterList getToCPE() {
    return toCPE;
  }

  public void setToCPE(ParameterList toCPE) {
    this.toCPE = toCPE;
  }

  /** All parameters that shall be written to the DB. */
  public List<ParameterValueStruct> getToDB() {
    return toDB;
  }

  /** All parameters requested from CPE. */
  public List<ParameterValueStruct> getRequestedCPE() {
    return requestedCPE;
  }

  public void setRequestedCPE(List<ParameterValueStruct> requestedCPE) {
    this.requestedCPE = requestedCPE;
  }

  /** Job. */
  @Override
  public dbi.Job getJob() {
    return job;
  }

  @Override
  public void setJob(dbi.Job job) {
    this.job = job;
  }

  /** All parameters from a job. */
  @Override
  public Map<String, JobParameter> getJobParams() {
    return jobParams;
  }

  @Override
  public void setJobParams(Map<String, JobParameter> jobParams) {
    this.jobParams = jobParams;
  }

  /** Parameterkey contains a hash of all values sent to CPE. */
  public ParameterKey getParameterKey() {
    return parameterKey;
  }

  public void setParameterKey(ParameterKey parameterKey) {
    this.parameterKey = parameterKey;
  }

  /** Commandkey contains the version number of the last download - if a download was sent. */
  public CommandKey getCommandKey() {
    return commandKey;
  }

  public void setCommandKey(CommandKey commandKey) {
    this.commandKey = commandKey;
  }

  /** Provisioning allowed. False if outside servicewindow or not allowed by unitJob */
  public boolean isProvisioningAllowed() {
    return provisioningAllowed;
  }

  public void setProvisioningAllowed(boolean provisioningAllowed) {
    this.provisioningAllowed = provisioningAllowed;
  }

  /** The secret obtained by discovery-mode, basic auth. */
  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  /** The flag signals a first-time connect in discovery-mode. */
  public boolean isFirstConnect() {
    return firstConnect;
  }

  public void setFirstConnect(boolean firstConnect) {
    this.firstConnect = firstConnect;
  }

  /** Unittype has been created, but unitId remains unknown, only for discovery-mode. */
  public boolean isUnittypeCreated() {
    return unittypeCreated;
  }

  public void setUnittypeCreated(boolean unittypeCreated) {
    this.unittypeCreated = unittypeCreated;
  }

  /** PIIDecision is important to decide the final outcome of the next Periodic Inform Interval. */
  public PIIDecision getPiiDecision() {
    return piiDecision;
  }

  public void setPiiDecision(PIIDecision piiDecision) {
    this.piiDecision = piiDecision;
  }

  /** An object to store all kinds of data about the provisioning. */
  @Override
  public dbi.util.ProvisioningMessage getProvisioningMessage() {
    return provisioningMessage;
  }

  public void setProvisioningMessage(dbi.util.ProvisioningMessage provisioningMessage) {
    this.provisioningMessage = provisioningMessage;
  }

  /** An object to store data about a download. */
  public Download getDownload() {
    return download;
  }

  public void setDownload(Download download) {
    this.download = download;
  }

  public String getCwmpVersionNumber() {
    return cwmpVersionNumber;
  }

  public void setCwmpVersionNumber(String cwmpVersionNumber) {
    this.cwmpVersionNumber = cwmpVersionNumber;
  }

  /** When did the session start? */
  @Override
  public Long getStartupTmsForSession() {
    return startupTmsForSession;
  }

  public void setStartupTmsForSession(Long startupTmsForSession) {
    this.startupTmsForSession = startupTmsForSession;
  }

  /** Data for monitoring/logging. */
  public List<HTTPRequestResponseData> getReqResList() {
    return reqResList;
  }

  public static class Download {
    private final String url;
    private final dbi.File file;

    public Download(String url, dbi.File file) {
      this.url = url;
      this.file = file;
    }

    public String getUrl() {
      return url;
    }

    public dbi.File getFile() {
      return file;
    }
  }
}
