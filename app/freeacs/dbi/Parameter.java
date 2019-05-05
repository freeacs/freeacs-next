package freeacs.dbi;

import java.util.regex.Pattern;

public class Parameter {

  private UnittypeParameter unittypeParameter;
  private String value;
  private Pattern pattern;
  private Integer groupParameterId;
  private Operator op;
  private ParameterDataType type;
  /**
   * If value = null, it is set to "". Introducing UnitParameterQuery, we want to be able to use
   * null-values in seraches, but we don't want to break the interface/behaviour (yet). instead we
   * keep this flag to inform UPQ about the value orginial state.
   */
  private boolean valueWasNull;

  public Parameter(UnittypeParameter utp, String val, Operator op, ParameterDataType type) {
    this.unittypeParameter = utp;
    setValue(val);
    this.op = op;
    this.type = type;
  }

  public Parameter(UnittypeParameter utp, String val) {
    this.unittypeParameter = utp;
    setValue(val);
    this.op = Operator.EQ;
    this.type = ParameterDataType.TEXT;
  }

  public UnittypeParameter getUnittypeParameter() {
    return unittypeParameter;
  }

  public void setUnittypeParameter(UnittypeParameter unittypeParameter) {
    this.unittypeParameter = unittypeParameter;
  }

  public String getValue() {
    if (value == null) {
      value = "";
    }
    return value;
  }

  public void setValue(String value) {
    if (value == null) {
      value = "";
      valueWasNull = true;
    } else {
      valueWasNull = false;
    }
    this.value = value;
  }

  public String toString() {
    return "[" + unittypeParameter.getName() + " " + op.getOperatorSign() + " " + getValue() + "]";
  }

  protected Pattern getPattern() {
    return pattern;
  }

  protected void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public Integer getGroupParameterId() {
    return groupParameterId;
  }

  public void setGroupParameterId(Integer groupParameterId) {
    this.groupParameterId = groupParameterId;
  }

  public Operator getOp() {
    return op;
  }

  public void setOp(Operator op) {
    this.op = op;
  }

  public ParameterDataType getType() {
    return type;
  }

  public void setType(ParameterDataType type) {
    this.type = type;
  }

  public boolean valueWasNull() {
    return valueWasNull;
  }

  public void setValueWasNull(boolean valueWasNull) {
    this.valueWasNull = valueWasNull;
  }
}
