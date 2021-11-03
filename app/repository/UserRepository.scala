package repository

import domain.`object`.user.NewUser.NewUserDto
import domain.`object`.user._
import repository.dao.UserDao

import scala.util.Try

class UserRepository(userDao: UserDao = new UserDao) {

  def findAll(): Try[Seq[User]] =
    userDao.selectAll().map { dtos =>
      dtos.map(User(_))
    }

  def findBy(userId: UserId): Try[User] =
    userDao.selectBy(userId.value).map(User(_))

  def findBy(email: Email): Try[User] = userDao.selectBy(email.value).map(User(_))

  def save(newUser: NewUser): Try[Int] = {
    val newUserDto = NewUserDto(
      newUser.name.value,
      newUser.email.value
    )
    userDao.insert(newUserDto)
  }

  def saveAndGetUserId(newUser: NewUser): Try[UserId] = {
    val newUserDto = NewUserDto(
      newUser.name.value,
      newUser.email.value
    )
    userDao.insertAndGetId(newUserDto).map(UserId)
  }

  def updateName(userId: UserId, userName: UserName): Try[Int] =
    userDao.updateName(userId.value, userName.value)

  def removeBy(userId: UserId): Try[Int] =
    userDao.deleteBy(userId.value)
}
