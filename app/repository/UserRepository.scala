package repository

import repository.dao.UserDao
import domain.`object`.user.{ User, UserId, UserName }
import play.api.db.DBApi

class UserRepository(dbApi: DBApi) {

  private val userDao = new UserDao(dbApi)

  def findAll(): Seq[User] =
    userDao.selectAll().map { dto =>
      User(
        UserId(dto.id),
        UserName(dto.name)
      )
    }
}
