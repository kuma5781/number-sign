package controllers

import domain.`object`.user.{ NewUser, User, UserId, UserName }
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

    implicit val userNameWrites = new Writes[UserName] {
      def writes(userName: UserName): JsObject = Json.obj("name" -> userName.value)
    }
  }

  "#index" should {
    "return Json phased Users" in new Context {
      val users = Seq(User(UserId(1), UserName("太郎")), User(UserId(2), UserName("次郎")))
      userService.findAll() returns Success(users)

      val home = userController.index().apply(FakeRequest(GET, "/user"))

      status(home) mustBe OK
      contentAsString(home) mustBe "[{\"id\":1,\"name\":\"太郎\"},{\"id\":2,\"name\":\"次郎\"}]"
    }

    "return NotFound error" in new Context {
      val exception = new Exception(s"DB connection error")
      userService.findAll() returns Failure(exception)

      val home = userController.index().apply(FakeRequest(GET, "/user"))

      status(home) mustBe NOT_FOUND
      contentAsString(home) mustBe exception.toString
    }
  }

  "#show" should {
    "return Json phased User" in new Context {
      val user = User(UserId(1), UserName("太郎"))
      userService.findBy(UserId(1)) returns Success(user)

      val home = userController.show(1).apply(FakeRequest(GET, "/user/1"))

      status(home) mustBe OK
      contentAsString(home) mustBe "{\"id\":1,\"name\":\"太郎\"}"
    }

    "return NotFound error" in new Context {
      val exception = new Exception(s"DB connection error")
      userService.findBy(UserId(1)) returns Failure(exception)

      val home = userController.show(1).apply(FakeRequest(GET, "/user/1"))

      status(home) mustBe NOT_FOUND
      contentAsString(home) mustBe exception.toString
    }
  }

  "#save" should {
    "return OK" in new Context {
      val userName = UserName("太郎")
      val newUser = NewUser(userName)
      userService.save(newUser) returns Success(1)

      val home = userController.save().apply(FakeRequest(POST, "/user").withJsonBody(Json.toJson(userName)))

      status(home) mustBe OK
      contentAsString(home) mustBe "User record saved successfully"
    }

    "return NotFound error" in new Context {
      val userName = UserName("太郎")
      val newUser = NewUser(userName)
      val exception = new Exception(s"DB connection error")
      userService.save(newUser) returns Failure(exception)

      val home = userController.save().apply(FakeRequest(POST, "/user").withJsonBody(Json.toJson(userName)))

      status(home) mustBe NOT_FOUND
      contentAsString(home) mustBe exception.toString
    }
  }

  "#update" should {
    "return OK" in new Context {
      val userName = UserName("太郎")
      val user = User(UserId(1), userName)
      userService.update(user) returns Success(1)

      val home = userController.update(1).apply(FakeRequest(PUT, "/user").withJsonBody(Json.toJson(userName)))

      status(home) mustBe OK
      contentAsString(home) mustBe "User record updated successfully"
    }

    "return NotFound error" in new Context {
      val userName = UserName("太郎")
      val user = User(UserId(1), userName)
      val exception = new Exception(s"DB connection error")
      userService.update(user) returns Failure(exception)

      val home = userController.update(1).apply(FakeRequest(PUT, "/user").withJsonBody(Json.toJson(userName)))

      status(home) mustBe NOT_FOUND
      contentAsString(home) mustBe exception.toString
    }
  }

  "#remove" should {
    "return OK" in new Context {
      userService.removeBy(UserId(1)) returns Success(1)

      val home = userController.remove(1).apply(FakeRequest(DELETE, "/user"))

      status(home) mustBe OK
      contentAsString(home) mustBe "User record removed successfully"
    }

    "return NotFound error" in new Context {
      val exception = new Exception(s"DB connection error")
      userService.removeBy(UserId(1)) returns Failure(exception)

      val home = userController.remove(1).apply(FakeRequest(DELETE, "/user"))

      status(home) mustBe NOT_FOUND
      contentAsString(home) mustBe exception.toString
    }
  }
}
