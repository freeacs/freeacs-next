package tr069.methods.response;

import tr069.http.HTTPRequestResponseData;
import tr069.xml.EmptyResponse;
import tr069.xml.Response;

public class EmptyResponseCreateStrategy implements ResponseCreateStrategy {
    @Override
    public Response getResponse(HTTPRequestResponseData reqRes) {
        return new EmptyResponse(reqRes.getSessionData().getCwmpVersionNumber());
    }
}
