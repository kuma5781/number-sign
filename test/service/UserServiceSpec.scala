package service

import domain.`object`.user.{ Email, NewUser, User, UserId, UserName }
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.UserRepository

import scala.util.{ Failure, Success }

class UserServiceSpec extends PlaySpec with MockitoSugar {

  trait Context {
    val userRepository = mock[UserRepository]
    val userService = new UserService(userRepository)

    val userId1 = UserId(1)
    val userName1 = UserName("太郎")
    val email1 = Email("taro@xxx.com")
    val user1 = User(userId1, userName1, email1)
    val newUser1 = NewUser(userName1, email1)

    val userId2 = UserId(2)
    val userName2 = UserName("次郎")
    val email2 = Email("jiro@xxx.com")
    val user2 = User(userId2, userName2, email2)

    val users = Seq(user1, user2)
  }

  "#findAll" should {
    "return all Users" in new Context {
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
      userRepository.findBy(userId1) returns Success(user1)
      userService.findBy(userId1) mustBe Success(user1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userRepository.findBy(userId1) returns Failure(exception)
      userService.findBy(userId1) mustBe Failure(exception)
    }
  }

  "#save" should {
    "return Success" in new Context {
      userRepository.save(newUser1) returns Success(1)
      userService.save(newUser1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userRepository.save(newUser1) returns Failure(exception)
      userService.save(newUser1) mustBe Failure(exception)
    }
  }

  "#update" should {
    "return Success" in new Context {
      userRepository.updateName(userId1, userName1) returns Success(1)
      userService.updateName(userId1, userName1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userRepository.updateName(userId1, userName1) returns Failure(exception)
      userService.updateName(userId1, userName1) mustBe Failure(exception)
    }
  }

  "#removeBy" should {
    "return Success" in new Context {
      userRepository.removeBy(userId1) returns Success(1)
      userService.removeBy(userId1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userRepository.removeBy(userId1) returns Failure(exception)
      userService.removeBy(userId1) mustBe Failure(exception)
    }
  }
}
