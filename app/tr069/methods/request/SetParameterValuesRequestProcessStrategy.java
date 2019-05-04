package tr069.methods.request;

import dbi.SyslogConstants;
import dbi.util.SyslogClient;
import tr069.SessionData;
import tr069.http.HTTPRequestResponseData;
import tr069.methods.ProvisioningMethod;
import tr069.xml.ParameterList;
import tr069.xml.ParameterValueStruct;
import tr069.xml.Parser;

public class SetParameterValuesRequestProcessStrategy implements RequestProcessStrategy {

    private final dbi.DBI dbi;

    SetParameterValuesRequestProcessStrategy(dbi.DBI dbi) {
        this.dbi = dbi;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void process(HTTPRequestResponseData reqRes) throws Exception {
        reqRes.getRequestData().setMethod(ProvisioningMethod.SetParameterValues.name());
        Parser parser = new Parser(reqRes.getRequestData().getXml());
        if (parser.getHeader().getNoMoreRequests() != null
                && parser.getHeader().getNoMoreRequests().getNoMoreRequestFlag()) {
            reqRes.getSessionData().setNoMoreRequests(true);
        }
        SessionData sessionData = reqRes.getSessionData();
        ParameterList paramList = sessionData.getToCPE();
        for (ParameterValueStruct pvs : paramList.getParameterValueList()) {
            log.debug(pvs.getName() + " : " + pvs.getValue());
            String user = dbi.getSyslog().getIdentity().getUser().getUsername();
            SyslogClient.notice(
                    sessionData.getUnitId(),
                    "ProvMsg: Written to CPE: " + pvs.getName() + " = " + pvs.getValue(),
                    SyslogConstants.FACILITY_TR069,
                    "latest",
                    user);
        }
    }
}
