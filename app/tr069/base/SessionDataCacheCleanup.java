package tr069.base;

import com.github.freeacs.common.util.CleanupNotifier;
import com.github.freeacs.dbi.util.ProvisioningMessage.ErrorResponsibility;
import com.github.freeacs.dbi.util.ProvisioningMessage.ProvOutput;
import com.github.freeacs.dbi.util.ProvisioningMessage.ProvStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionDataCacheCleanup implements CleanupNotifier {
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

    com.github.freeacs.dbi.util.ProvisioningMessage pm = sessionData.getProvisioningMessage();
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
        com.github.freeacs.dbi.util.SyslogClient.send(pm.syslogMsg(16, null, com.github.freeacs.dbi.Users.USER_ADMIN));
      } catch (Throwable t) {
        log.error("Could not send/make syslog-message in SessionDataCacheCleanup");
      }
    }
  }
}
