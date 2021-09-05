package controllers

import domain.`object`.user.{ NewUser, User, UserId, UserName }
import global.JsonSupport.{ reads, RichRequest }
import global.ResultSupport.RichResult
import javax.inject._
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json._
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

  implicit val userNameReads = reads("name", UserName)

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

  def save(): Action[AnyContent] = {
    Action { request =>
      val maybeUserName = request.getObject[UserName]
      val result = maybeUserName match {
        case Success(userName) =>
          val newUser = NewUser(userName)
          userService.save(newUser) match {
            case Success(_) => Ok("User record saved successfully")
            case Failure(e) => NotFound(e.toString)
          }
        case Failure(e) => NotFound(e.toString)
      }
      result.enableCors
    }
  }

  def update(userId: Int): Action[AnyContent] = {
    Action { request =>
      val maybeUserName = request.getObject[UserName]
      val result = maybeUserName match {
        case Success(userName) =>
          val user = User(UserId(userId), userName)
          userService.update(user) match {
            case Success(_) => Ok("User record updated successfully")
            case Failure(e) => NotFound(e.toString)
          }
        case Failure(e) => NotFound(e.toString)
      }
      result.enableCors
    }
  }

  def remove(userId: Int): Action[AnyContent] =
    Action {
      val result = userService.removeBy(UserId(userId)) match {
        case Success(_) => Ok("User record deleted successfully")
        case Failure(e) => NotFound(e.toString)
      }
      result.enableCors
    }
}
