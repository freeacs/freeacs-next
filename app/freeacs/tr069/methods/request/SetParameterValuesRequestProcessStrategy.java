package freeacs.tr069.methods.request;

import freeacs.dbi.DBI;
import freeacs.dbi.SyslogConstants;
import freeacs.dbi.util.SyslogClient;
import freeacs.tr069.SessionData;
import freeacs.tr069.http.HTTPRequestResponseData;
import freeacs.tr069.methods.ProvisioningMethod;
import freeacs.tr069.xml.ParameterList;
import freeacs.tr069.xml.ParameterValueStruct;
import freeacs.tr069.xml.Parser;

public class SetParameterValuesRequestProcessStrategy implements RequestProcessStrategy {

    private final DBI dbi;

    SetParameterValuesRequestProcessStrategy(DBI dbi) {
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
