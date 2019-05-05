package freeacs.tr069.base;

import freeacs.dbi.JobParameter;
import freeacs.dbi.util.ProvisioningMessage;
import freeacs.tr069.xml.ParameterValueStruct;

import java.util.Map;

public interface SessionDataI {
  ACSParameters getAcsParameters();

  void setAcsParameters(ACSParameters acsParameters);

  freeacs.dbi.Unittype getUnittype();

  void setUnittype(freeacs.dbi.Unittype unittype);

  freeacs.dbi.Profile getProfile();

  void setProfile(freeacs.dbi.Profile profile);

  freeacs.dbi.Unit getUnit();

  void setUnit(freeacs.dbi.Unit unit);

  String getUnitId();

  void setUnitId(String unitId);

  freeacs.dbi.Job getJob();

  void setJob(freeacs.dbi.Job job);

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

  ProvisioningMessage getProvisioningMessage();

  Long getStartupTmsForSession();
}
