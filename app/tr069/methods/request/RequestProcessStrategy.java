package tr069.methods.request;

import dbi.DBI;
import tr069.Properties;
import tr069.base.BaseCache;
import tr069.http.HTTPRequestResponseData;
import tr069.methods.ProvisioningMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionalInterface
public interface RequestProcessStrategy {
    Logger log = LoggerFactory.getLogger(RequestProcessStrategy.class);

    void process(HTTPRequestResponseData reqRes) throws Exception;

    static RequestProcessStrategy getStrategy(ProvisioningMethod provisioningMethod, Properties properties, DBI dbi, BaseCache baseCache) {
        switch (provisioningMethod) {
            case Empty: return doNotProcessStrategy();
            case Download: return downloadStrategy();
            case Fault: return faultStrategy();
            case FactoryReset: return factoryResetStrategy();
            case Inform: return informStrategy(properties, dbi, baseCache);
            case GetParameterNames: return getParameterNamesStrategy(properties, dbi);
            case GetParameterValues: return getParameterValuesStrategy();
            case SetParameterValues: return setParameterValuesStrategy(dbi);
            case TransferComplete: return transferCompleteStrategy();
            case AutonomousTransferComplete: return autonomousTransferComplete();
            case Reboot: return rebootStrategy();
            default:
                log.debug("The methodName " + provisioningMethod + " has no request processing strategy");
                return doNotProcessStrategy();
        }
    }

    static RequestProcessStrategy factoryResetStrategy() {
        return new FactoryResetRequestProcessStrategy();
    }

    static RequestProcessStrategy rebootStrategy() {
        return new RebootRequestProcessStrategy();
    }

    static RequestProcessStrategy downloadStrategy() {
        return new DownloadRequestProcessStrategy();
    }

    static RequestProcessStrategy faultStrategy() {
        return new FaultRequestProcessStrategy();
    }

    static RequestProcessStrategy autonomousTransferComplete() {
        return new AutonomousTransferCompleteRequestProcessStrategy();
    }

    static RequestProcessStrategy transferCompleteStrategy() {
        return new TransferCompleteRequestProcessStrategy();
    }

    static RequestProcessStrategy doNotProcessStrategy() {
        return reqRes -> {};
    }

    static RequestProcessStrategy informStrategy(Properties properties, DBI dbi, BaseCache baseCache) {
        return new InformRequestProcessStrategy(properties, dbi, baseCache);
    }

    static RequestProcessStrategy getParameterNamesStrategy(Properties properties, dbi.DBI dbi) {
        return new GetParameterNamesProcessStrategy(properties, dbi);
    }

    static RequestProcessStrategy getParameterValuesStrategy() {
        return new GetParameterValuesRequestProcessStrategy();
    }

    static RequestProcessStrategy setParameterValuesStrategy(dbi.DBI dbi) {
        return new SetParameterValuesRequestProcessStrategy(dbi);
    }
}
