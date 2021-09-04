package controllers

import domain.`object`.user.{ User, UserId, UserName }
import org.powermock.reflect.Whitebox
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.specs2.mock.Mockito.theStubbed
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
}
