package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def upload = Action(parse.multipartFormData) { request =>

    request.body.file("analysis").map { analysis =>
      import java.io.File
      val filename = analysis.filename
      val contentType = analysis.contentType
      analysis.ref.moveTo(new File(s"/tmp/picture/$filename"))
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file")
    }
  }
}
