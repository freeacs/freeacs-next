package freeacs.tr069;

import freeacs.tr069.xml.ParameterValueStruct;
import java.util.HashMap;
import java.util.Map;

public class InformParameters {
  public final String UDP_CONNECTION_URL;

  private final Map<String, ParameterValueStruct> cpeParams = new HashMap<>();

  public InformParameters(String keyRoot) {
    UDP_CONNECTION_URL = keyRoot + "ManagementServer.UDPConnectionRequestAddress";
    cpeParams.put(UDP_CONNECTION_URL, null);
  }

  public String getValue(String param) {
    ParameterValueStruct pvs = cpeParams.get(param);
    if (pvs != null && pvs.getValue() != null) {
      return pvs.getValue();
    } else {
      return null;
    }
  }

  /** The connection udp-url (for kick, ip-address). */
  public String getUDP_CONNECTION_URL() {
    return UDP_CONNECTION_URL;
  }

  public Map<String, ParameterValueStruct> getCpeParams() {
    return cpeParams;
  }
}
