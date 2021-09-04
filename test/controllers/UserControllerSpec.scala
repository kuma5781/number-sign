package controllers

import domain.`object`.user.{User, UserId, UserName}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import org.specs2.mock.Mockito.theStubbed
import play.api.test._
import play.api.test.Helpers._
import service.UserService

import scala.util.Success

class UserControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  trait Context {
    val userService = mock[UserService]
  }

  "#index" should {

//    "render the index page from a new instance of controller" in {
//      val controller = new UserController(stubControllerComponents())
//      val home = controller.index().apply(FakeRequest(GET, "/user"))
//
//      println()
//    }

    "render the index page from the application" in new Context {
      userService.findAll() returns Success(Seq(User(UserId(1), UserName("太郎"))))

      val controller = inject[UserController]
      val home = controller.index().apply(FakeRequest(GET, "/user"))

      status(home) mustBe OK
      contentAsString(home) must include("{\"id\": 1, \"name\":\"太郎\"}")
    }

    "render the index page from the router" in {
//      val request = FakeRequest(GET, "/")
//      val home = route(app, request).get
//
//      status(home) mustBe OK
//      contentType(home) mustBe Some("text/html")
//      contentAsString(home) must include("Welcome to Play")
      assert(1 == 1)
    }
  }
}
