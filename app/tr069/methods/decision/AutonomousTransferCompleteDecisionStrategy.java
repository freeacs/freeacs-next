package tr069.methods.decision;

import tr069.http.HTTPRequestResponseData;
import tr069.methods.ProvisioningMethod;
import tr069.xml.Fault;

public class AutonomousTransferCompleteDecisionStrategy implements DecisionStrategy {
    @SuppressWarnings("Duplicates")
    @Override
    public void makeDecision(HTTPRequestResponseData reqRes) {
        try {
            Fault fault = reqRes.getRequestData().getFault();
            if (fault != null && !"0".equals(fault.getFaultCode())) {
                String errormsg = "ATC request reports a faultcode (" + fault.getFaultCode();
                errormsg += ") with faultstring (" + fault.getFaultString() + ")";
                log.error(errormsg);
            }
        } finally {
            reqRes.getResponseData().setMethod(ProvisioningMethod.AutonomousTransferComplete.name());
        }
    }
}
