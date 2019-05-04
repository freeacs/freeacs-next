package tr069.background;

import common.scheduler.TaskDefaultImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListenerTask extends TaskDefaultImpl {
  private static Logger logger = LoggerFactory.getLogger(MessageListenerTask.class);
  private com.github.freeacs.dbi.Inbox tr069ServerListenerInbox = new com.github.freeacs.dbi.Inbox();

  public MessageListenerTask(String taskName, com.github.freeacs.dbi.DBI dbi) {
    super(taskName);
    tr069ServerListenerInbox.addFilter(new com.github.freeacs.dbi.Message(null, null, dbi.SyslogConstants.FACILITY_TR069, null));
    dbi.registerInbox("tr069ServerListener", tr069ServerListenerInbox);
  }

  @Override
  public void runImpl() {
    tr069ServerListenerInbox.deleteReadMessage();
  }

  @Override
  public Logger getLogger() {
    return logger;
  }
}
