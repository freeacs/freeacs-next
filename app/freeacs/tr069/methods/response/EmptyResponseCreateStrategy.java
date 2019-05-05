package freeacs.tr069.methods.response;

import freeacs.tr069.http.HTTPRequestResponseData;
import freeacs.tr069.xml.EmptyResponse;
import freeacs.tr069.xml.Response;

public class EmptyResponseCreateStrategy implements ResponseCreateStrategy {
    @Override
    public Response getResponse(HTTPRequestResponseData reqRes) {
        return new EmptyResponse(reqRes.getSessionData().getCwmpVersionNumber());
    }
}
