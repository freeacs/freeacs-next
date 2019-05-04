package controllers

import dbi.DBIHolder
import javax.inject._
import play.api.Configuration
import play.api.mvc._
import tr069.Properties
import tr069.base.BaseCache
import tr069.http.HTTPRequestResponseData
import tr069.methods.ProvisioningStrategy

@Singleton
class Tr069Controller @Inject()(cc: ControllerComponents,
                                properties: Properties,
                                baseCache: BaseCache,
                                config: Configuration,
                                dbiHolder: DBIHolder) extends AbstractController(cc) {

  def provision: Action[AnyContent] = Action { req =>
    val session = getSession(req)
    val reqRes = new HTTPRequestResponseData(baseCache, req.headers.get("REMOTE_ADDR").orNull, req.headers.get("X-Real-IP").orNull, session("uuid"))
    reqRes.getRequestData.setContextPath(config.getString("context-path").getOrElse("/"))
    reqRes.getRequestData.setXml(req.body.asXml.map(_.toString).getOrElse(""))
    ProvisioningStrategy.getStrategy(properties, dbiHolder.dbi, baseCache).process(reqRes)
    Ok(reqRes.getResponseData.getXml).withHeaders("SOAPAction" -> "").withSession(session)
  }

  private def getSession(req: Request[AnyContent]): Session = {
    req.session.get("uuid").map(_ => req.session).getOrElse {
      req.session + ("uuid" -> java.util.UUID.randomUUID.toString)
    }
  }
}
