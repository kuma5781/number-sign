package controllers

import domain.`object`.user.User
import global.ResultSupport.RichResult
import javax.inject._
import play.api.db._
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{ Json, Writes }
import play.api.mvc._
import service.UserService

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(dbApi: DBApi, val controllerComponents: ControllerComponents) extends BaseController {

  private val userService = new UserService(dbApi)

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id.value,
      "name" -> user.name.value
    )
  }

  def index(): Action[AnyContent] =
    Action {
      val users = userService.findAll()
      Ok(Json.toJson(users)).enableCors
    }
}
