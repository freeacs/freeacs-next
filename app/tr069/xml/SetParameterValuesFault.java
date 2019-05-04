package tr069.xml;

public class SetParameterValuesFault {
  private String faultCode;
  private String faultString;
  private String parameterName;

  public String getFaultCode() {
    return faultCode;
  }

  public void setFaultCode(String faultCode) {
    this.faultCode = faultCode;
  }

  public String getFaultString() {
    return faultString;
  }

  public void setFaultString(String faultString) {
    this.faultString = faultString;
  }

  public String getParameterName() {
    return parameterName;
  }

  public void setParameterName(String parameterName) {
    this.parameterName = parameterName;
  }

  public SetParameterValuesFault(String faultCode, String faultString, String parameterName) {
    this.faultCode = faultCode;
    this.faultString = faultString;
    this.parameterName = parameterName;
  }

  public String toString() {
    StringBuffer str = new StringBuffer("--- SetParameterValueFault>");
    str.append("  ParameterName: ").append(this.parameterName);
    str.append("  FaultCode: ").append(this.faultCode);
    str.append("  FaultString: ").append(this.faultString);

    return String.valueOf(str);
  }
}
