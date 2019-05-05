package freeacs.tr069.methods.request;

import freeacs.tr069.http.HTTPRequestResponseData;
import freeacs.tr069.methods.ProvisioningMethod;
import freeacs.tr069.xml.Header;
import freeacs.tr069.xml.Parser;

public class AutonomousTransferCompleteRequestProcessStrategy implements RequestProcessStrategy {
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
