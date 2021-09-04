package repository

import domain.`object`.user.{NewUser, User, UserId, UserName}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito.theStubbed
import repository.dao.{NewUserDto, UserDao, UserDto}

import scala.util.{Failure, Success}

class UserRepositorySpec extends PlaySpec with MockitoSugar {

  trait Context {
    val userDao = mock[UserDao]

    val userRepository = new UserRepository(userDao)
  }

  "#findAll" should {
    "return all Users" in new Context {
      val userDtos = Seq(UserDto(1, "太郎"), UserDto(2, "次郎"))
      val users = Seq(User(UserId(1), UserName("太郎")), User(UserId(2), UserName("次郎")))
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
    "return User record associated with userId" in new Context {
      val userDto = UserDto(1, "太郎")
      val user = User(UserId(1), UserName("太郎"))
      userDao.selectBy(1) returns Success(userDto)
      userRepository.findBy(UserId(1)) mustBe Success(user)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userDao.selectBy(1) returns Failure(exception)
      userRepository.findBy(UserId(1)) mustBe Failure(exception)
    }
  }

  "#save" should {
    "return Success" in new Context {
      val newUSerDto = NewUserDto("太郎")
      val newUser = NewUser(UserName("太郎"))
      userDao.insert(newUSerDto) returns Success(1)
      userRepository.save(newUser) mustBe Success(1)
    }

    "return Exception" in new Context {
      val newUSerDto = NewUserDto("太郎")
      val newUser = NewUser(UserName("太郎"))
      val exception = new Exception(s"DB connection error")
      userDao.insert(newUSerDto) returns Failure(exception)
      userRepository.save(newUser) mustBe Failure(exception)
    }
  }

  "#update" should {
    "return Success" in new Context {
      val userDto = UserDto(1, "太郎")
      val user = User(UserId(1), UserName("太郎"))
      userDao.update(userDto) returns Success(1)
      userRepository.update(user) mustBe Success(1)
    }

    "return Exception" in new Context {
      val userDto = UserDto(1, "太郎")
      val user = User(UserId(1), UserName("太郎"))
      val exception = new Exception(s"DB connection error")
      userDao.update(userDto) returns Failure(exception)
      userRepository.update(user) mustBe Failure(exception)
    }
  }

  "#removeBy" should {
    "return Success" in new Context {
      userDao.deleteBy(1) returns Success(1)
      userRepository.removeBy(UserId(1)) mustBe Success(1)
    }

    "return Exception" in new Context {
      val exception = new Exception(s"DB connection error")
      userDao.deleteBy(1) returns Failure(exception)
      userRepository.removeBy(UserId(1)) mustBe Failure(exception)
    }
  }
}
