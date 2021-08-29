package repository

import domain.`object`.user.{User, UserId, UserName}
import play.api.db.DBApi
import repository.dao.UserDao

import scala.util.Try

class UserRepository(dbApi: DBApi) {

  private val userDao = new UserDao(dbApi)

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
}
