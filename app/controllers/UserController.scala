package controllers

import domain.`object`.user.User
import global.ResultSupport.RichResult
import javax.inject._
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{ JsObject, Json, Writes }
import play.api.mvc._
import service.UserService

@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val userService = new UserService
  implicit val userWrites = new Writes[User] {
    def writes(user: User): JsObject = Json.obj(
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
