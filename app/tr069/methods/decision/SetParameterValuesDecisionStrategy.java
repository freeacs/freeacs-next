package tr069.methods.decision;

import dbi.UnitJobStatus;
import dbi.util.ProvisioningMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr069.Properties;
import tr069.SessionData;
import tr069.base.UnitJob;
import tr069.http.HTTPRequestResponseData;
import tr069.methods.ProvisioningMethod;

public class SetParameterValuesDecisionStrategy implements DecisionStrategy {
    private static final Logger log = LoggerFactory.getLogger(SetParameterValuesDecisionStrategy.class);

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
        if (sessionData.getUnit().getProvisioningMode() == ProvisioningMode.REGULAR
                && properties.isParameterkeyQuirk(sessionData)
                && sessionData.isProvisioningAllowed()) {
            log.debug("UnitJob is COMPLETED without verification stage, since CPE does not support ParameterKey");
            UnitJob uj = new UnitJob(sessionData, dbi, sessionData.getJob(), false);
            uj.stop(UnitJobStatus.COMPLETED_OK, properties.isDiscoveryMode());
        }
        reqRes.getResponseData().setMethod(ProvisioningMethod.Empty.name());
    }
}
