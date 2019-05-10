package controllers

import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class UserRequest[A](val username: Option[String], request: Request[A]) extends WrappedRequest[A](request)

class UserAction(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
    extends ActionBuilder[UserRequest, AnyContent]
    with ActionTransformer[Request, UserRequest] {
  def transform[A](request: Request[A]) = Future.successful {
    new UserRequest(request.session.get("username"), request)
  }
}
