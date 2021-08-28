package repository

import domain.`object`.user.{ User, UserId, UserName }
import repository.dao.UserDao

class UserRepository() {

  private val userDao = new UserDao

  def findAll(): Seq[User] =
    userDao.selectAll().map { dto =>
      User(
        UserId(dto.id),
        UserName(dto.name)
      )
    }
}
