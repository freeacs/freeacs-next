package controllers
import play.api.Logging
import play.api.mvc.{AbstractController, ControllerComponents}
import views.Page

class RedirectController(cc: ControllerComponents) extends AbstractController(cc) with Logging {
  def index(path: String, page: Page) = Action {
    logger.warn(
      s"Tried to access unimplemented $path, redirecting to " + page.getClass.getSimpleName.stripSuffix("$")
    )
    Redirect(page.url)
  }
}
