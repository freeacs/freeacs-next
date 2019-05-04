package tr069.methods.decision;

import tr069.Properties;
import tr069.SessionData;
import tr069.base.UnitJob;
import tr069.http.HTTPRequestResponseData;
import tr069.methods.ProvisioningMethod;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetParameterValuesDecisionStrategy implements DecisionStrategy {
    private final Properties properties;
    private final dbi.DBI dbi;

    SetParameterValuesDecisionStrategy(Properties properties, dbi.DBI dbi) {
        this.properties = properties;
        this.dbi = dbi;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void makeDecision(HTTPRequestResponseData reqRes) {
        SessionData sessionData = reqRes.getSessionData();
        if (sessionData.getUnit().getProvisioningMode() == dbi.util.ProvisioningMode.REGULAR
                && properties.isParameterkeyQuirk(sessionData)
                && sessionData.isProvisioningAllowed()) {
            log.debug("UnitJob is COMPLETED without verification stage, since CPE does not support ParameterKey");
            UnitJob uj = new UnitJob(sessionData, dbi, sessionData.getJob(), false);
            uj.stop(dbi.UnitJobStatus.COMPLETED_OK, properties.isDiscoveryMode());
        }
        reqRes.getResponseData().setMethod(ProvisioningMethod.Empty.name());
    }
}
