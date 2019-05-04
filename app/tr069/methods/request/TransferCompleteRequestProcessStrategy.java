package tr069.methods.request;

import tr069.http.HTTPRequestResponseData;
import tr069.methods.ProvisioningMethod;
import tr069.xml.Header;
import tr069.xml.Parser;

public class TransferCompleteRequestProcessStrategy implements RequestProcessStrategy {
    @SuppressWarnings("Duplicates")
    @Override
    public void process(HTTPRequestResponseData reqRes) throws Exception {
        reqRes.getRequestData().setMethod(ProvisioningMethod.TransferComplete.name());
        Parser parser = new Parser(reqRes.getRequestData().getXml());
        Header header = parser.getHeader();
        reqRes.setTR069TransactionID(header.getId());
        if (parser.getFault() != null && !"0".equals(parser.getFault().getFaultCode())) {
            reqRes.getRequestData().setFault(parser.getFault());
            log.debug("TCReq reported a fault");
        } else {
            log.debug("TCReq is ok (download is assumed ok)");
        }
    }
}
