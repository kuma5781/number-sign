package service

import domain.`object`.user.{ User, UserId, UserName }
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.UserRepository

import scala.util.Success

class UserServiceSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val userRepository = mock[UserRepository]

    val userService = new UserService(userRepository)
  }

  "#findAll" should {
    "return all Users" in new Context {
      val users = Seq(User(UserId(1), UserName("太郎")), User(UserId(2), UserName("次郎")))
      userRepository.findAll() returns Success(users)

      userService.findAll() mustBe Success(users)
    }
  }
}
