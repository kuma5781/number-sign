package service

import domain.`object`.user.{User, UserId, UserName}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.UserRepository

import scala.util.{Failure, Success}

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

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userRepository.findAll() returns Failure(exception)

      userService.findAll() mustBe Failure(exception)
    }
  }

  "#findBy" should {
    "return a user associated with userId" in new Context {
      val user = User(UserId(1), UserName("太郎"))
      userRepository.findBy(UserId(1)) returns Success(user)

      userService.findBy(UserId(1)) mustBe Success(user)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userRepository.findBy(UserId(1)) returns Failure(exception)

      userService.findBy(UserId(1)) mustBe Failure(exception)
    }
  }
}
