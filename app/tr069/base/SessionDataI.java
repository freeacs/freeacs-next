package tr069.base;

import com.github.freeacs.dbi.JobParameter;
import com.github.freeacs.tr069.xml.ParameterValueStruct;

import java.util.Map;

public interface SessionDataI {
  ACSParameters getAcsParameters();

  void setAcsParameters(ACSParameters acsParameters);

  com.github.freeacs.dbi.Unittype getUnittype();

  void setUnittype(com.github.freeacs.dbi.Unittype unittype);

  com.github.freeacs.dbi.Profile getProfile();

  void setProfile(com.github.freeacs.dbi.Profile profile);

  com.github.freeacs.dbi.Unit getUnit();

  void setUnit(com.github.freeacs.dbi.Unit unit);

  String getUnitId();

  void setUnitId(String unitId);

  com.github.freeacs.dbi.Job getJob();

  void setJob(com.github.freeacs.dbi.Job job);

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

  com.github.freeacs.dbi.util.ProvisioningMessage getProvisioningMessage();

  Long getStartupTmsForSession();
}
