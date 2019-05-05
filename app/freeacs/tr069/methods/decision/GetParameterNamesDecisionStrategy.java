package freeacs.tr069.methods.decision;

import freeacs.tr069.http.HTTPRequestResponseData;
import freeacs.tr069.methods.ProvisioningMethod;

public class GetParameterNamesDecisionStrategy implements DecisionStrategy {
    @Override
    public void makeDecision(HTTPRequestResponseData reqRes) {
        reqRes.getResponseData().setMethod(ProvisioningMethod.GetParameterValues.name());
    }
}
