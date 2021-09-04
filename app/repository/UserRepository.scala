package repository

import domain.`object`.user.{NewUser, User, UserId, UserName}
import repository.dao.{NewUserDto, UserDao, UserDto}

import scala.util.Try

class UserRepository(userDao: UserDao = new UserDao) {

  def findAll(): Try[Seq[User]] = {
    userDao.selectAll().map { dtos =>
      dtos.map { dto =>
        User(
          UserId(dto.id),
          UserName(dto.name)
        )
      }
    }
  }

  def findBy(userId: UserId): Try[User] = {
    userDao.selectBy(userId.value).map { dto =>
      User(
        UserId(dto.id),
        UserName(dto.name)
      )
    }
  }

  def save(newUser: NewUser): Try[Int] = {
    val newUserDto = NewUserDto(newUser.name.value)
    userDao.insert(newUserDto)
  }

  def update(user: User): Try[Int] = {
    val userDto = UserDto(
      user.id.value,
      user.name.value
    )
    userDao.update(userDto)
  }

  def removeBy(userId: UserId): Try[Int] = {
    userDao.deleteBy(userId.value)
  }
}
