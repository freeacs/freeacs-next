package tr069.methods.response;

import tr069.http.HTTPRequestResponseData;
import tr069.xml.Body;
import tr069.xml.Header;
import tr069.xml.Response;
import tr069.xml.TR069TransactionID;

public class RebootResponseCreateStrategy implements ResponseCreateStrategy {
    @SuppressWarnings("Duplicates")
    @Override
    public Response getResponse(HTTPRequestResponseData reqRes) {
        if (reqRes.getTR069TransactionID() == null) {
            reqRes.setTR069TransactionID(new TR069TransactionID("FREEACS-" + System.currentTimeMillis()));
        }
        TR069TransactionID tr069ID = reqRes.getTR069TransactionID();
        Header header = new Header(tr069ID, null, null);
        Body body = new Body() {
            @Override
            public String toXmlImpl() {
                return "\t<cwmp:Reboot xmlns:cwmp=\"urn:dslforum-org:cwmp-" + reqRes.getSessionData().getCwmpVersionNumber() + "\">\n" +
                        "\t\t<CommandKey>Reboot_FREEACS-" +
                        System.currentTimeMillis() +
                        "</CommandKey>\n" +
                        "\t</cwmp:Reboot>\n";
            }
        };
        return new Response(header, body, reqRes.getSessionData().getCwmpVersionNumber());
    }
}
