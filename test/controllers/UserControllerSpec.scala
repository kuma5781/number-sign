package controllers

import domain.`object`.user.{ Email, NewUser, User, UserId, UserName }
import org.powermock.reflect.Whitebox
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.specs2.mock.Mockito.theStubbed
import play.api.libs.json.{ JsObject, Json, Writes }
import play.api.test.Helpers._
import play.api.test._
import service.UserService

import scala.util.{ Failure, Success }

class UserControllerSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val userService = mock[UserService]

    val userController = new UserController(stubControllerComponents())
    // userServiceフィールドをmock
    Whitebox.setInternalState(userController, "userService", userService)

    val userIdDto1 = 1
    val userId1 = UserId(userIdDto1)
    val userName1 = UserName("taro")
    val email1 = Email("taro@xxx.com")
    val user1 = User(userId1, userName1, email1)
    val newUser1 = NewUser(userName1, email1)

    val userId2 = UserId(2)
    val userName2 = UserName("jiro")
    val email2 = Email("jiro@xxx.com")
    val user2 = User(userId2, userName2, email2)

    val users = Seq(user1, user2)

    implicit val userNameWrites = new Writes[UserName] {
      def writes(userName: UserName): JsObject = Json.obj("name" -> userName.value)
    }

    implicit val newUserWrites = new Writes[NewUser] {
      def writes(newUser: NewUser): JsObject = Json.obj(
        "name" -> newUser.name.value,
        "email" -> newUser.email.value
      )
    }
  }

  "#index" should {
    "return Json phased Users" in new Context {
      userService.findAll() returns Success(users)

      val home = userController.index().apply(FakeRequest(GET, "/user"))

      status(home) mustBe OK
      contentAsString(home) mustBe "[{\"id\":1,\"name\":\"taro\",\"email\":\"taro@xxx.com\"},{\"id\":2,\"name\":\"jiro\",\"email\":\"jiro@xxx.com\"}]"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      userService.findAll() returns Failure(exception)

      val home = userController.index().apply(FakeRequest(GET, "/user"))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#show" should {
    "return Json phased User" in new Context {
      userService.findBy(userId1) returns Success(user1)

      val home = userController.show(userIdDto1).apply(FakeRequest(GET, s"/user/$userIdDto1"))

      status(home) mustBe OK
      contentAsString(home) mustBe "{\"id\":1,\"name\":\"taro\",\"email\":\"taro@xxx.com\"}"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      userService.findBy(userId1) returns Failure(exception)

      val home = userController.show(userIdDto1).apply(FakeRequest(GET, s"/user/$userIdDto1"))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#showFrom" should {
    "return Json phased User" in new Context {
      userService.findBy(email1) returns Success(user1)

      val home = userController.showFrom(email1.value).apply(FakeRequest(GET, s"/user/email/$email1"))
      status(home) mustBe OK
      contentAsString(home) mustBe "{\"id\":1,\"name\":\"taro\",\"email\":\"taro@xxx.com\"}"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      userService.findBy(email1) returns Failure(exception)

      val home = userController.showFrom(email1.value).apply(FakeRequest(GET, s"/user/email/$email1"))
      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#save" should {
    "return OK" in new Context {
      userService.save(newUser1) returns Success(1)

      val home = userController.save().apply(FakeRequest(POST, "/user").withJsonBody(Json.toJson(newUser1)))

      status(home) mustBe OK
      contentAsString(home) mustBe "User saved successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      userService.save(newUser1) returns Failure(exception)

      val home = userController.save().apply(FakeRequest(POST, "/user").withJsonBody(Json.toJson(newUser1)))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#updateName" should {
    "return OK" in new Context {
      userService.updateName(userId1, userName1) returns Success(1)

      val home =
        userController
          .updateName(userIdDto1)
          .apply(FakeRequest(PUT, s"/user/name/$userIdDto1").withJsonBody(Json.toJson(userName1)))

      status(home) mustBe OK
      contentAsString(home) mustBe "User updated successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      userService.updateName(userId1, userName1) returns Failure(exception)

      val home =
        userController
          .updateName(userIdDto1)
          .apply(FakeRequest(PUT, s"/user/name/$userIdDto1").withJsonBody(Json.toJson(userName1)))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }

  "#remove" should {
    "return OK" in new Context {
      userService.removeBy(userId1) returns Success(1)

      val home = userController.remove(userIdDto1).apply(FakeRequest(DELETE, s"/user/$userIdDto1"))

      status(home) mustBe OK
      contentAsString(home) mustBe "User removed successfully"
    }

    "return BadRequest" in new Context {
      val exception = new Exception("DB connection error")
      userService.removeBy(userId1) returns Failure(exception)

      val home = userController.remove(userIdDto1).apply(FakeRequest(DELETE, s"/user/$userIdDto1"))

      status(home) mustBe BAD_REQUEST
      contentAsString(home) mustBe exception.toString
    }
  }
}
