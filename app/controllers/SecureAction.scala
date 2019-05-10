package controllers

import com.github.jarlah.authenticscala.{AuthenticationContext, Authenticator}
import com.github.jarlah.authenticscala.Authenticator.challenge
import com.typesafe.config.Config
import play.api.Logging
import play.api.mvc._
import play.api.mvc.Results._
import services.UnitService

import scala.concurrent.{ExecutionContext, Future}

class SecureRequest[A](val username: Option[String], override val session: Session, val sessionId: String, request: Request[A])
    extends WrappedRequest[A](request)

class SecureAction(unitDetails: UnitService, config: Config, parser: BodyParser[AnyContent])(implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser)
    with Logging {

  val SESSION_KEY = "uuid"

  def protect = this andThen transform

  override def invokeBlock[A](req: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    logger.debug(s"HTTP request: ${req.method} ${req.uri}")
    val authMethod = config.getString("auth.method")
    val session    = getSession(req)
    val sessionId  = session.get(SESSION_KEY).get
    authMethod.toLowerCase match {
      case method if method == "digest" && config.getString("digest.secret") == "changeme" =>
        logger.error("Digest secret must be changed from its default value!")
        Future.successful(InternalServerError(""))
      case method if "none" != method =>
        val context = getContext(req)
        (for {
          result <- authenticate(method, context)
        } yield {
          if (result.success) {
            block(new SecureRequest(result.principal, session, sessionId, req))
          } else {
            Future.successful(Unauthorized.withHeaders(challenge(context, method).toSeq: _*))
          }
        }).flatten
      case _ =>
        logger.debug(s"Allowing request to pass through. Auth method is $authMethod")
        block(new SecureRequest(None, session, sessionId, req))
    }
  }

  private def transform: ActionTransformer[Request, SecureRequest] =
    new ActionTransformer[Request, SecureRequest] {
      override protected def executionContext: ExecutionContext = ec
      override protected def transform[A](request: Request[A]): Future[SecureRequest[A]] =
        Future.successful(request.asInstanceOf[SecureRequest[A]])
    }

  private def getSession[A](request: Request[A]): Session =
    request.session
      .get(SESSION_KEY)
      .map(_ => request.session)
      .getOrElse(request.session + (SESSION_KEY -> java.util.UUID.randomUUID.toString))

  private def getContext[A](req: Request[A]) =
    AuthenticationContext(
      req.method,
      req.uri,
      req.headers.toSimpleMap,
      req.remoteAddress
    )

  private def authenticate[A](method: String, context: AuthenticationContext) =
    Authenticator.authenticate(
      context,
      (unitId: String) => unitDetails.getSecret(unitId).map(_.orNull),
      method
    )
}
