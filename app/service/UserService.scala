package service

import domain.`object`.user.{ NewUser, User, UserId, UserName, Email }
import repository.UserRepository

import scala.util.Try

class UserService(userRepository: UserRepository = new UserRepository) {

  def findAll(): Try[Seq[User]] = userRepository.findAll()
  def findBy(userId: UserId): Try[User] = userRepository.findBy(userId)
  def save(newUser: NewUser): Try[Int] = userRepository.save(newUser)
  def saveEmail(email: Email): Try[Int] = {
    val newUser = NewUser(UserName.generateFrom(email), email)
    userRepository.save(newUser)
  }
  def updateName(userId: UserId, userName: UserName): Try[Int] = userRepository.updateName(userId, userName)
  def removeBy(userId: UserId): Try[Int] = userRepository.removeBy(userId)
}
