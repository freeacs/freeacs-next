package freeacs.tr069.methods.request;

import freeacs.tr069.http.HTTPRequestResponseData;
import freeacs.tr069.methods.ProvisioningMethod;
import freeacs.tr069.xml.Parser;

public class FaultRequestProcessStrategy implements RequestProcessStrategy {
    @Override
    public void process(HTTPRequestResponseData reqRes) throws Exception {
        reqRes.getRequestData().setMethod(ProvisioningMethod.Fault.name());
        Parser parser = new Parser(reqRes.getRequestData().getXml());
        reqRes.getRequestData().setFault(parser.getFault());
    }
}
