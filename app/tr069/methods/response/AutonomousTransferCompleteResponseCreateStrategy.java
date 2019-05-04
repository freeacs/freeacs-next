package tr069.methods.response;

import tr069.http.HTTPRequestResponseData;
import tr069.xml.Body;
import tr069.xml.Header;
import tr069.xml.Response;
import tr069.xml.TR069TransactionID;

public class AutonomousTransferCompleteResponseCreateStrategy implements ResponseCreateStrategy {
    @SuppressWarnings("Duplicates")
    @Override
    public Response getResponse(HTTPRequestResponseData reqRes) {
        TR069TransactionID tr069ID = reqRes.getTR069TransactionID();
        Header header = new Header(tr069ID, null, null);
        Body body = new Body() {
            @Override
            public String toXmlImpl() {
                return "\t\t<cwmp:AutonomousTransferCompleteResponse />\n";
            }
        };
        return new Response(header, body, reqRes.getSessionData().getCwmpVersionNumber());
    }
}
