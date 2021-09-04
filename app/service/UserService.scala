package service

import domain.`object`.user.{ User, UserId }
import repository.UserRepository

import scala.util.Try

class UserService(userRepository: UserRepository = new UserRepository) {

  def findAll(): Try[Seq[User]] = userRepository.findAll()
  def findBy(userId: UserId): Try[User] = userRepository.findBy(userId)
}
