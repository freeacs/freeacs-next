package freeacs.tr069.xml;


import java.util.ArrayList;
import java.util.List;

public class MethodList {
  private List<String> methods = new ArrayList<>();

  public void addMethod(String method) {
    this.methods.add(method);
  }

  public boolean contains(String method) {
    return methods.contains(method);
  }
}
