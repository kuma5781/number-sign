package service

import domain.`object`.user.{User, UserId}
import repository.UserRepository

import scala.util.Try

class UserService() {

  private val userRepository = new UserRepository

  def findAll(): Try[Seq[User]] = userRepository.findAll()
  def findBy(userId: UserId): Try[User] = userRepository.findBy(userId)
}
