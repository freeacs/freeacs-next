package tr069.methods.decision;

import tr069.http.HTTPRequestResponseData;
import tr069.methods.ProvisioningMethod;

public class GetRPCMethodsDecisionStrategy implements DecisionStrategy {
    @Override
    public void makeDecision(HTTPRequestResponseData reqRes) {
        reqRes.getResponseData().setMethod(ProvisioningMethod.GetRPCMethods.name());
    }
}
