package tr069.xml;

public class ParameterInfoStruct {
  private String name;
  private boolean writable;
  private boolean inspect;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isWritable() {
    return writable;
  }

  public void setWritable(boolean writable) {
    this.writable = writable;
  }

  public boolean isInspect() {
    return inspect;
  }

  public void setInspect(boolean inspect) {
    this.inspect = inspect;
  }
}
