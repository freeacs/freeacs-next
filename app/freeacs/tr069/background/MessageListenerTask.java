package freeacs.tr069.background;

import freeacs.common.scheduler.TaskDefaultImpl;
import freeacs.dbi.DBI;
import freeacs.dbi.Inbox;
import freeacs.dbi.Message;
import freeacs.dbi.SyslogConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListenerTask extends TaskDefaultImpl {
  private static Logger logger = LoggerFactory.getLogger(MessageListenerTask.class);
  private Inbox tr069ServerListenerInbox = new Inbox();

  public MessageListenerTask(String taskName, DBI dbi) {
    super(taskName);
    tr069ServerListenerInbox.addFilter(new Message(null, null, SyslogConstants.FACILITY_TR069, null));
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
