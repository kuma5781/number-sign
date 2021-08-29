package controllers

import domain.`object`.user.{ User, UserId }
import global.ResultSupport.RichResult
import javax.inject._
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{ JsObject, Json, Writes }
import play.api.mvc._
import service.UserService

import scala.util.{ Failure, Success }

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
      val result = userService.findAll() match {
        case Success(users) => Ok(Json.toJson(users))
        case Failure(e) => NotFound(e.toString)
      }
      result.enableCors
    }

  def show(userId: Int): Action[AnyContent] =
    Action {
      val result = userService.findBy(UserId(userId)) match {
        case Success(user) => Ok(Json.toJson(user))
        case Failure(e) => NotFound(e.toString)
      }
      result.enableCors
    }
}
