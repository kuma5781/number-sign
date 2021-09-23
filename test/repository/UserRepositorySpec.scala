package repository

import domain.`object`.user.NewUser.NewUserDto
import domain.`object`.user.User.UserDto
import domain.`object`.user.{ Email, NewUser, User, UserId, UserName }
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.dao.UserDao

import scala.util.{ Failure, Success }

class UserRepositorySpec extends PlaySpec with MockitoSugar {

  trait Context {
    val userDao = mock[UserDao]
    val userRepository = new UserRepository(userDao)

    val userIdDto1 = 1
    val userNameDto1 = "太郎"
    val emailDto1 = "taro@xxx.com"
    val userDto1 = UserDto(userIdDto1, userNameDto1, emailDto1)
    val newUserDto1 = NewUserDto(userNameDto1, emailDto1)

    val userId1 = UserId(userIdDto1)
    val userName1 = UserName(userNameDto1)
    val email1 = Email(emailDto1)
    val user1 = User(userId1, userName1, email1)
    val newUser1 = NewUser(userName1, email1)

    val userIdDto2 = 2
    val userNameDto2 = "次郎"
    val emailDto2 = "jiro@xxx.com"
    val userDto2 = UserDto(userIdDto2, userNameDto2, emailDto2)

    val userId2 = UserId(userIdDto2)
    val userName2 = UserName(userNameDto2)
    val email2 = Email(emailDto2)
    val user2 = User(userId2, userName2, email2)

    val userDtos = Seq(userDto1, userDto2)
    val users = Seq(user1, user2)
  }

  "#findAll" should {
    "return all Users" in new Context {
      userDao.selectAll() returns Success(userDtos)
      userRepository.findAll() mustBe Success(users)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userDao.selectAll() returns Failure(exception)
      userRepository.findAll() mustBe Failure(exception)
    }
  }

  "#findBy" should {
    "return User associated with userId" in new Context {
      userDao.selectBy(userIdDto1) returns Success(userDto1)
      userRepository.findBy(userId1) mustBe Success(user1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userDao.selectBy(userIdDto1) returns Failure(exception)
      userRepository.findBy(userId1) mustBe Failure(exception)
    }
  }

  "#save" should {
    "return Success" in new Context {
      userDao.insert(newUserDto1) returns Success(1)
      userRepository.save(newUser1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userDao.insert(newUserDto1) returns Failure(exception)
      userRepository.save(newUser1) mustBe Failure(exception)
    }
  }

  "#updateName" should {
    "return Success" in new Context {
      userDao.updateName(userIdDto1, userNameDto1) returns Success(1)
      userRepository.updateName(userId1, userName1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userDao.updateName(userIdDto1, userNameDto1) returns Failure(exception)
      userRepository.updateName(userId1, userName1) mustBe Failure(exception)
    }
  }

  "#removeBy" should {
    "return Success" in new Context {
      userDao.deleteBy(userIdDto1) returns Success(1)
      userRepository.removeBy(userId1) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userDao.deleteBy(userIdDto1) returns Failure(exception)
      userRepository.removeBy(userId1) mustBe Failure(exception)
    }
  }
}
