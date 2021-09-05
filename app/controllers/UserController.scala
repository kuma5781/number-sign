package controllers

import domain.`object`.user.{ NewUser, User, UserId, UserName }
import global.ResultSupport.RichResult
import javax.inject._
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.{ JsObject, Json, Writes }
import play.api.mvc._
import service.UserService

import scala.util.{ Failure, Success }

@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  val userService: UserService = new UserService

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

  def save(userName: String): Action[AnyContent] = {
    val newUser = NewUser(UserName(userName))
    Action {
      val result = userService.save(newUser) match {
        case Success(_) => Ok("User record saved successfully")
        case Failure(e) => NotFound(e.toString)
      }
      result.enableCors
    }
  }

  def update(userId: Int, userName: String): Action[AnyContent] = {
    val user = User(UserId(userId), UserName(userName))
    Action {
      val result = userService.update(user) match {
        case Success(_) => Ok("User record updated successfully")
        case Failure(e) => NotFound(e.toString)
      }
      result.enableCors
    }
  }

}
