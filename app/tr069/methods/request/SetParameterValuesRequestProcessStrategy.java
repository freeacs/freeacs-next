package tr069.methods.request;

import com.github.freeacs.tr069.SessionData;
import com.github.freeacs.tr069.http.HTTPRequestResponseData;
import com.github.freeacs.tr069.methods.ProvisioningMethod;
import com.github.freeacs.tr069.xml.ParameterList;
import com.github.freeacs.tr069.xml.ParameterValueStruct;
import com.github.freeacs.tr069.xml.Parser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetParameterValuesRequestProcessStrategy implements RequestProcessStrategy {

    private final com.github.freeacs.dbi.DBI dbi;

    SetParameterValuesRequestProcessStrategy(com.github.freeacs.dbi.DBI dbi) {
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
            com.github.freeacs.dbi.util.SyslogClient.notice(
                    sessionData.getUnitId(),
                    "ProvMsg: Written to CPE: " + pvs.getName() + " = " + pvs.getValue(),
                    com.github.freeacs.dbi.SyslogConstants.FACILITY_TR069,
                    "latest",
                    user);
        }
    }
}
