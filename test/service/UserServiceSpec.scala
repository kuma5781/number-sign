package service

import domain.`object`.user.{NewUser, User, UserId, UserName}
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

  "#save" should {
    "return Success" in new Context {
      val newUser = NewUser(UserName("太郎"))
      userRepository.save(newUser) returns Success(1)
      userService.save(newUser) mustBe Success(1)
    }

    "return Exception" in new Context {
      val newUser = NewUser(UserName("太郎"))
      val exception = new Exception(s"DB connection error")
      userRepository.save(newUser) returns Failure(exception)
      userService.save(newUser) mustBe Failure(exception)
    }
  }

  "#update" should {
    "return Success" in new Context {
      val user = User(UserId(1), UserName("太郎"))
      userRepository.update(user) returns Success(1)
      userService.update(user) mustBe Success(1)
    }

    "return Exception" in new Context {
      val user = User(UserId(1), UserName("太郎"))
      val exception = new Exception(s"DB connection error")
      userRepository.update(user) returns Failure(exception)
      userService.update(user) mustBe Failure(exception)
    }
  }

  "#removeBy" should {
    "return Success" in new Context {
      userRepository.removeBy(UserId(1)) returns Success(1)
      userService.removeBy(UserId(1)) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userRepository.removeBy(UserId(1)) returns Failure(exception)
      userService.removeBy(UserId(1)) mustBe Failure(exception)
    }
  }
}
