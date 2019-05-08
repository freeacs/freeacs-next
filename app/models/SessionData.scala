package models
import play.api.mvc.{AnyContent, Request}

case class SessionData(sessionId: String, unitId: String)

object SessionData {
  def apply(req: Request[AnyContent], unitId: String): SessionData = {
    req.session.get("uuid").map(_ => new SessionData(req.session("uuid"), unitId)).getOrElse {
      new SessionData(java.util.UUID.randomUUID.toString, unitId)
    }
  }
}
