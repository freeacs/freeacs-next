package tr069.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
  private static Logger eventLog = LoggerFactory.getLogger("Event");
  private static Logger convLog = LoggerFactory.getLogger("Conversation");
  private static Logger debugLog = LoggerFactory.getLogger("Debug");

  public static void conversation(SessionDataI sessionData, String message) {
    convLog.info(message);
    if (sessionData == null
        || sessionData.getUnit() == null
        || sessionData.getUnit().getUnitParameters() == null) {
      return;
    }
    com.github.freeacs.dbi.UnitParameter debugUp = sessionData.getUnit().getUnitParameters().get(com.github.freeacs.dbi.util.SystemParameters.DEBUG);
    if (debugUp != null && "1".equals(debugUp.getValue())) {
      debugLog.info(message);
    }
  }

  public static boolean isConversationLogEnabled() {
    return convLog.isDebugEnabled() || convLog.isInfoEnabled();
  }

  public static void event(String message) {
    eventLog.info(message);
  }
}
