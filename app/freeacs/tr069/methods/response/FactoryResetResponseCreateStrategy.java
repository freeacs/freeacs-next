package freeacs.tr069.methods.response;

import freeacs.tr069.http.HTTPRequestResponseData;
import freeacs.tr069.xml.Body;
import freeacs.tr069.xml.Header;
import freeacs.tr069.xml.Response;
import freeacs.tr069.xml.TR069TransactionID;

public class FactoryResetResponseCreateStrategy implements ResponseCreateStrategy {
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
                return "\t<cwmp:FactoryReset xmlns:cwmp=\"urn:dslforum-org:cwmp-" + reqRes.getSessionData().getCwmpVersionNumber() + "\">\n" +
                        "\t</cwmp:FactoryReset>\n";
            }
        };
        return new Response(header, body, reqRes.getSessionData().getCwmpVersionNumber());
    }
}