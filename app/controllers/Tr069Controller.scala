package controllers

import com.github.jarlah.authenticscala.Authenticator._
import com.github.jarlah.authenticscala.{AuthenticationContext, Authenticator}
import com.typesafe.config.Config
import freeacs.dbi.DBIHolder
import services.UnitDetailsService
import play.api.Logging
import play.api.mvc._
import freeacs.tr069.Properties
import freeacs.tr069.base.BaseCache
import freeacs.tr069.http.HTTPRequestResponseData
import freeacs.tr069.methods.ProvisioningStrategy

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class Tr069Controller(cc: ControllerComponents,
                                properties: Properties,
                                baseCache: BaseCache,
                                config: Config,
                                dbiHolder: DBIHolder,
                                unitDetails: UnitDetailsService)(implicit ec: ExecutionContext) extends AbstractController(cc) with Logging {

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
            (u: String) => Try(unitDetails.loadUserByUsername(u)).toEither match {
              case Right(user) => Future.successful(user.pass)
              case Left(error) => Future.failed(error)
            },
            method
          )
        } yield {
          if (result.success) {
            processRequest(req, result.principal)
          } else {
            Unauthorized.withHeaders(challenge(context, method).toSeq: _*)
          }
        }
      case _ => Future.successful(processRequest(req))
    }
  }

  private def processRequest(req: Request[AnyContent], principal: Option[String] = None): Result = {
    val session = getSession(req)
    val realIp = req.headers.get("X-Real-IP").orNull
    val reqRes = new HTTPRequestResponseData(baseCache, req.remoteAddress, realIp, session("uuid"))
    reqRes.getRequestData.setContextPath(config.getString("context-path"))
    reqRes.getRequestData.setXml(req.body.asXml.map(_.toString).getOrElse(""))
    if (principal.isDefined && reqRes.getSessionData.getUnit == null) {
      val username = principal.get
      val sessionData = reqRes.getSessionData
      sessionData.setUnitId(username)
      sessionData.setUnit(dbiHolder.dbi.getACSUnit.getUnitById(username))
    }
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
