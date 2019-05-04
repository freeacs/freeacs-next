package tr069.base;

import dbi.JobParameter;
import tr069.xml.ParameterValueStruct;

import java.util.Map;

public interface SessionDataI {
  ACSParameters getAcsParameters();

  void setAcsParameters(ACSParameters acsParameters);

  dbi.Unittype getUnittype();

  void setUnittype(dbi.Unittype unittype);

  dbi.Profile getProfile();

  void setProfile(dbi.Profile profile);

  dbi.Unit getUnit();

  void setUnit(dbi.Unit unit);

  String getUnitId();

  void setUnitId(String unitId);

  dbi.Job getJob();

  void setJob(dbi.Job job);

  Map<String, JobParameter> getJobParams();

  void setJobParams(Map<String, JobParameter> jobParams);

  String getSoftwareVersion();

  void setSoftwareVersion(String softwareVersion);

  Map<String, ParameterValueStruct> getFromDB();

  void setFromDB(Map<String, ParameterValueStruct> fromDB);

  boolean lastProvisioningOK();

  /**
   * The PIIDecision object contains information need to calculate the next periodic inform. The
   * information needed in this object is listed in the javadoc for that class. Make sure the method
   * never return null!!
   *
   * @return
   */
  PIIDecision getPIIDecision();

  void setSerialNumber(String serialNumber);

  String getSerialNumber();

  dbi.util.ProvisioningMessage getProvisioningMessage();

  Long getStartupTmsForSession();
}
