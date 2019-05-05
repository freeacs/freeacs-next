package freeacs.tr069.base;

import freeacs.common.util.CleanupNotifier;
import freeacs.dbi.util.ProvisioningMessage;
import freeacs.dbi.util.ProvisioningMessage.ErrorResponsibility;
import freeacs.dbi.util.ProvisioningMessage.ProvOutput;
import freeacs.dbi.util.ProvisioningMessage.ProvStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionDataCacheCleanup implements CleanupNotifier {
  private static final Logger log = LoggerFactory.getLogger(SessionDataCacheCleanup.class);

  private SessionDataI sessionData;
  private String unitKey;

  SessionDataCacheCleanup(String unitKey, SessionDataI sessionData) {
    this.unitKey = unitKey;
    this.sessionData = sessionData;
  }

  @Override
  public void execute() {
    log.error("SessionData for "
            + sessionData.getUnitId()
            + " (key:"
            + unitKey
            + ") was removed from cache after timeout - indicate a session which did not terminate correctly");

    ProvisioningMessage pm = sessionData.getProvisioningMessage();
    if (pm != null) { // only available if run in a TR-069 server (not in SPP-server)
      pm.setErrorMessage(
          "TR-069 session was aborted - most probably because the client did not respond");
      pm.setErrorResponsibility(ErrorResponsibility.CLIENT);
      pm.setProvStatus(ProvStatus.ERROR);
      if (pm.getProvOutput() == null) {
        pm.setProvOutput(ProvOutput.EMPTY);
      }
      if (sessionData.getStartupTmsForSession() != null) {
        pm.setSessionLength(
            (int) (System.currentTimeMillis() - sessionData.getStartupTmsForSession()));
      }
      try {
        freeacs.dbi.util.SyslogClient.send(pm.syslogMsg(16, null, freeacs.dbi.Users.USER_ADMIN));
      } catch (Throwable t) {
        log.error("Could not send/make syslog-message in SessionDataCacheCleanup");
      }
    }
  }
}
