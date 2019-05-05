package freeacs.tr069.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import freeacs.tr069.SessionData;
import freeacs.tr069.base.BaseCache;
import freeacs.tr069.base.BaseCacheException;
import freeacs.tr069.xml.TR069TransactionID;
import java.util.Optional;

public class HTTPRequestResponseData {

  private static final Logger log = LoggerFactory.getLogger(HTTPRequestResponseData.class);

  private HTTPRequestData requestData;

  private HTTPResponseData responseData;

  private String remoteHost;

  private final String realIp;

  private Throwable throwable;

  private TR069TransactionID TR069TransactionID;

  private SessionData sessionData;

  public HTTPRequestResponseData(BaseCache cache, String remoteHost, String realIp, String sessionId) {
    this.remoteHost = remoteHost;
    this.realIp = realIp;
    this.requestData = new HTTPRequestData();
    this.responseData = new HTTPResponseData();
    try {
      sessionData = (SessionData) cache.getSessionData(sessionId);
    } catch (BaseCacheException tr069Ex) {
      log.debug("Sessionid " + sessionId + " did not return a SessionData object from cache, must create a new SessionData object");
      sessionData = new SessionData(sessionId);
      cache.putSessionData(sessionId, sessionData);
    }
    if (sessionData.getStartupTmsForSession() == null) {
      sessionData.setStartupTmsForSession(System.currentTimeMillis());
    }
    log.debug("Adding a HTTPReqResData object to the list");
    sessionData.getReqResList().add(this);
  }

  public HTTPRequestData getRequestData() {
    return requestData;
  }

  public HTTPResponseData getResponseData() {
    return responseData;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }

  public TR069TransactionID getTR069TransactionID() {
    return TR069TransactionID;
  }

  public void setTR069TransactionID(TR069TransactionID transactionID) {
    TR069TransactionID = transactionID;
  }

  public SessionData getSessionData() {
    return sessionData;
  }

  public String getRealIPAddress() {
    return Optional.ofNullable(realIp).orElseGet(() -> remoteHost);
  }
}
