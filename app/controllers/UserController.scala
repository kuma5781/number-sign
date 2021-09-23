package controllers

import domain.`object`.user.NewUser.NewUserDto
import domain.`object`.user.{ NewUser, User, UserId, UserName }
import javax.inject._
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json._
import play.api.mvc._
import service.UserService
import supports.{ JsonSupport, ResultSupport }

import scala.util.{ Failure, Success }

@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController
  with JsonSupport
  with ResultSupport {

  val userService = new UserService

  implicit val userWrites = new Writes[User] {
    def writes(user: User): JsObject = Json.obj(
      "id" -> user.id.value,
      "name" -> user.name.value,
      "email" -> user.email.value
    )
  }

  implicit val userNameReads = reads("name", UserName)

  implicit val newUserDtoReads = (
    (JsPath \ "name").read[String] and
      (JsPath \ "email").read[String]
  )(NewUserDto)

  def index(): Action[AnyContent] =
    Action {
      val result = userService.findAll() match {
        case Success(users) => Ok(Json.toJson(users))
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }

  def show(userId: Int): Action[AnyContent] =
    Action {
      val result = userService.findBy(UserId(userId)) match {
        case Success(user) => Ok(Json.toJson(user))
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }

  def save(): Action[AnyContent] = {
    Action { request =>
      val maybeNewUserDto = request.getObject[NewUserDto]
      val result = maybeNewUserDto match {
        case Success(newUserDto) =>
          val newUser = NewUser(newUserDto)
          userService.save(newUser) match {
            case Success(_) => Ok("User record saved successfully")
            case Failure(e) => BadRequest(e.toString)
          }
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }
  }

  def updateName(userId: Int): Action[AnyContent] = {
    Action { request =>
      val maybeUserName = request.getObject[UserName]
      val result = maybeUserName match {
        case Success(userName) =>
          userService.updateName(UserId(userId), userName) match {
            case Success(_) => Ok("User record updated successfully")
            case Failure(e) => BadRequest(e.toString)
          }
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }
  }

  def remove(userId: Int): Action[AnyContent] =
    Action {
      val result = userService.removeBy(UserId(userId)) match {
        case Success(_) => Ok("User record removed successfully")
        case Failure(e) => BadRequest(e.toString)
      }
      result.enableCors
    }
}
