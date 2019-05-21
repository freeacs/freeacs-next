package controllers

import play.api.mvc._
import play.api.{Environment, Logging, Mode}

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Node

class LoggingAction(parser: BodyParser[AnyContent], env: Environment)(implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser)
    with Logging {

  val prettyPrinter = new scala.xml.PrettyPrinter(80, 4)

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    val body = getRequestBody(request)
    logger.info(s"Request ${request.method} ${request.path} from ${request.remoteAddress}: $body")
    block(request)
  }

  private def getRequestBody[A](request: Request[A]) =
    if (env.mode != Mode.Prod) {
      request.body match {
        case node: Node => s"\n${prettyPrinter.format(node)}"
        case _          => ""
      }
    } else {
      ""
    }
}
