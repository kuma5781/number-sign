package controllers

import domain.`object`.user.{ User, UserId, UserName }
import org.powermock.reflect.Whitebox
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.specs2.mock.Mockito.theStubbed
import play.api.libs.json.{ JsObject, Json, Writes }
import play.api.test.Helpers._
import play.api.test._
import service.UserService

import scala.util.Success

class UserControllerSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val userService = mock[UserService]

    val userController = new UserController(stubControllerComponents())

    implicit val userWrites = new Writes[User] {
      def writes(user: User): JsObject = Json.obj(
        "id" -> user.id.value,
        "name" -> user.name.value
      )
    }
  }

  "#index" should {
    "return Json phased Users" in new Context {
      val users = Seq(User(UserId(1), UserName("太郎")), User(UserId(2), UserName("次郎")))
      userService.findAll() returns Success(users)
      Whitebox.setInternalState(userController, "userService", userService)

      val home = userController.index().apply(FakeRequest(GET, "/user"))

      status(home) mustBe OK
      contentAsString(home) mustBe Json.toJson(users).toString()
    }
  }

  "#show" should {
    "return Json phased User" in new Context {
      val user = User(UserId(1), UserName("太郎"))
      userService.findBy(UserId(1)) returns Success(user)
      Whitebox.setInternalState(userController, "userService", userService)

      val home = userController.show(1).apply(FakeRequest(GET, "/user/1"))

      status(home) mustBe OK
      contentAsString(home) mustBe Json.toJson(user).toString()
    }
  }
}
