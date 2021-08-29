package service

import domain.`object`.user.{ User, UserId }
import play.api.db.DBApi
import repository.UserRepository

import scala.util.Try

class UserService(dbApi: DBApi) {

  private val userRepository = new UserRepository(dbApi)

  def findAll(): Try[Seq[User]] = userRepository.findAll()
  def findBy(userId: UserId): Try[User] = userRepository.findBy(userId)
}
