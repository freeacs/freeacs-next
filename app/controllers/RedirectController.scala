package controllers
import com.google.inject.Inject
import play.api.Logging
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import views.Page

class RedirectController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with Logging {
  def index(path: String, page: Page): Action[AnyContent] = Action {
    logger.warn(
      s"Tried to access unimplemented $path, redirecting to " + page.getClass.getSimpleName.stripSuffix("$")
    )
    Redirect(page.url)
  }
}
