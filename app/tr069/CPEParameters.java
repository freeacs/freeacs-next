package tr069;

import tr069.xml.ParameterValueStruct;

import java.util.HashMap;
import java.util.Map;

/**
 * These parameters holds a special meaning for the TR-069 Server. The server MUST know the value of
 * these CPE-parameters, regardless of what configuration is found in the database. If the database
 * only asks for parameter X and Y, but none of these parameters, the server will inject these
 * parameters in the GetParameterValueRequest to the CPE.
 *
 * <p>The reason for this special interest is that these parameters controls the following important
 * features of the server: - download (config/firmware) - periodic inform interval (spread/static) -
 * kick
 *
 * @author Morten
 */
public class CPEParameters {
  public final String CONFIG_FILES;
  public final String SOFTWARE_VERSION;
  public final String PERIODIC_INFORM_INTERVAL;
  public final String CONNECTION_URL;
  public final String CONNECTION_USERNAME;
  public final String CONNECTION_PASSWORD;

  private final Map<String, ParameterValueStruct> cpeParams = new HashMap<>();

  public CPEParameters(String keyRoot) {
    CONFIG_FILES = keyRoot + "DeviceInfo.VendorConfigFile.";
    SOFTWARE_VERSION = keyRoot + "DeviceInfo.SoftwareVersion";
    PERIODIC_INFORM_INTERVAL = keyRoot + "ManagementServer.PeriodicInformInterval";
    CONNECTION_URL = keyRoot + "ManagementServer.ConnectionRequestURL";
    CONNECTION_PASSWORD = keyRoot + "ManagementServer.ConnectionRequestPassword";
    CONNECTION_USERNAME = keyRoot + "ManagementServer.ConnectionRequestUsername";
    cpeParams.put(CONFIG_FILES, null);
    cpeParams.put(SOFTWARE_VERSION, null);
    cpeParams.put(PERIODIC_INFORM_INTERVAL, null);
    cpeParams.put(CONNECTION_URL, null);
    cpeParams.put(CONNECTION_USERNAME, null);
    cpeParams.put(CONNECTION_PASSWORD, null);
  }

  public String getValue(String param) {
    ParameterValueStruct pvs = cpeParams.get(param);
    if (pvs != null && pvs.getValue() != null) {
      return pvs.getValue();
    } else {
      return null;
    }
  }

  /**
   * Retrieves info from vendor-config-files and populate this map: Key: Name of config-file Value:
   * Version of config-file This method will crash with NP if the device returns a
   * VendorConfigFile.{i}.Name, but not the corresponding VendorConfigFile.{i}.Version
   */
  public Map<String, String> getConfigFileMap() {
    Map<String, String> cMap = new HashMap<>();
    cpeParams.forEach((namePN, value) -> {
      if (namePN.contains(CONFIG_FILES) && namePN.endsWith("Name")) {
        String namePV = value.getValue();
        String versionPN = namePN.substring(0, namePN.length() - "Name".length()) + "Version";
        ParameterValueStruct valueStruct = cpeParams.get(versionPN);
        if (valueStruct != null) {
          String versionPV = valueStruct.getValue();
          cMap.put(namePV, versionPV);
        }
      }
    });
    return cMap;
  }

  public Map<String, ParameterValueStruct> getCpeParams() {
    return cpeParams;
  }
}
