package freeacs.tr069.xml;


/**
 * TR069SessionID is a container of the TR-069 Session ID sent from the CPE to the ACS.
 *
 * @author morten
 */
public class TR069TransactionID {
  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TR069TransactionID() {
  }

  public TR069TransactionID(String id) {
    this.id = id;
  }

  String toXml() {
    StringBuilder sb = new StringBuilder(3);
      sb.append("\t<cwmp:ID ").append("soapenv").append(":mustUnderstand=\"1\">");
    sb.append(id);
    sb.append("</cwmp:ID>\n");
    return sb.toString();
  }
}
