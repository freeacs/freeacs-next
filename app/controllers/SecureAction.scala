package controllers

import com.github.jarlah.authenticscala.{AuthenticationContext, Authenticator}
import com.github.jarlah.authenticscala.Authenticator.challenge
import com.google.inject.Singleton
import config.AppConfig
import javax.inject.Inject
import play.api.{Configuration, Environment, Logging}
import play.api.mvc._
import play.api.mvc.Results._
import services.UnitService

import scala.concurrent.{ExecutionContext, Future}

class SecureRequest[A](
    val username: Option[String],
    override val session: Session,
    val sessionId: String,
    request: Request[A]
) extends WrappedRequest[A](request)

@Singleton
class SecureAction @Inject()(
    unitService: UnitService,
    config: Configuration,
    parser: BodyParsers.Default,
    loggingAction: LoggingAction
)(
    implicit ec: ExecutionContext
) extends ActionBuilderImpl(parser)
    with ActionRefiner[Request, SecureRequest]
    with Logging {

  private val sessionKey = "uuid"
  private val appConfig  = config.get[AppConfig]("app")

  val verify: ActionBuilder[SecureRequest, AnyContent] = loggingAction.andThen(this)

  override def refine[A](req: Request[A]): Future[Either[Result, SecureRequest[A]]] = {
    val authMethod = appConfig.authMethod
    val session    = getSession(req)
    val sessionId  = session.get(sessionKey).get
    authMethod.toLowerCase match {
      case method if method == "digest" && appConfig.digestSecret == "changeme" =>
        logger.error("Digest secret must be changed from its default value!")
        Future.successful(Left(InternalServerError("")))
      case method if "none" != method =>
        val context = getContext(req)
        authenticate(method, context).map { result =>
          if (result.success) {
            Right(new SecureRequest(result.principal, session, sessionId, req))
          } else {
            Left(Unauthorized.withHeaders(challenge(context, method).toSeq: _*))
          }
        }
      case _ =>
        logger.debug(s"Allowing request to pass through. Auth method is $authMethod")
        Future.successful(Right(new SecureRequest(None, session, sessionId, req)))
    }
  }

  private def getSession[A](request: Request[A]): Session =
    request.session
      .get(sessionKey)
      .map(_ => request.session)
      .getOrElse(request.session + (sessionKey -> java.util.UUID.randomUUID.toString))

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
      (unitId: String) => unitService.getSecret(unitId).map(_.orNull),
      method
    )

}
