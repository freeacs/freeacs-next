package controllers

import com.github.jarlah.authenticscala.Authenticator._
import com.github.jarlah.authenticscala.{AuthenticationContext, Authenticator}
import com.typesafe.config.Config
import dbi.DBIHolder
import javax.inject._
import play.api.Logging
import play.api.mvc._
import services.UnitDetailsService
import tr069.Properties
import tr069.base.BaseCache
import tr069.http.HTTPRequestResponseData
import tr069.methods.ProvisioningStrategy

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Tr069Controller @Inject()(implicit ec: ExecutionContext,
                                cc: ControllerComponents,
                                properties: Properties,
                                baseCache: BaseCache,
                                config: Config,
                                dbiHolder: DBIHolder,
                                unitDetails: UnitDetailsService) extends AbstractController(cc) with Logging {

  def provision: Action[AnyContent] = Action.async { req =>
    config.getString("auth.method").toLowerCase match {
      case method if method == "digest" && config.getString("digest.secret") == "changeme" =>
        logger.error("Digest secret must be changed from its default value!")
        Future.successful(InternalServerError(""))
      case method if "none" != method =>
        val context = AuthenticationContext(
          req.method,
          req.uri,
          req.headers.toSimpleMap,
          req.remoteAddress
        )
        for {
          result <- Authenticator.authenticate(
            context,
            (u: String) => Future.successful(unitDetails.loadUserByUsername(u).pass),
            method
          )
        } yield {
          if (result.success) {
            processRequest(req)
          } else {
            Unauthorized(result.errorMessage.getOrElse(""))
              .withHeaders(challenge(context, method).toSeq: _*)
          }
        }
      case _ => Future.successful(processRequest(req))
    }
  }

  private def processRequest(req: Request[AnyContent]): Result = {
    val session = getSession(req)
    val realIp = req.headers.get("X-Real-IP").orNull
    val reqRes = new HTTPRequestResponseData(baseCache, req.remoteAddress, realIp, session("uuid"))
    reqRes.getRequestData.setContextPath(config.getString("context-path"))
    reqRes.getRequestData.setXml(req.body.asXml.map(_.toString).getOrElse(""))
    ProvisioningStrategy.getStrategy(properties, dbiHolder.dbi, baseCache).process(reqRes)
    Ok(reqRes.getResponseData.getXml)
      .withHeaders("SOAPAction" -> "")
      .withSession(session)
  }

  private def getSession(req: Request[AnyContent]): Session = {
    req.session.get("uuid").map(_ => req.session).getOrElse {
      req.session + ("uuid" -> java.util.UUID.randomUUID.toString)
    }
  }
}
