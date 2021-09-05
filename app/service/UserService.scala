package service

import domain.`object`.user.{NewUser, User, UserId}
import repository.UserRepository

import scala.util.Try

class UserService(userRepository: UserRepository = new UserRepository) {

  def findAll(): Try[Seq[User]] = userRepository.findAll()
  def findBy(userId: UserId): Try[User] = userRepository.findBy(userId)
  def save(newUser: NewUser): Try[Int] = userRepository.save(newUser)
  def update(user: User): Try[Int] = userRepository.update(user)
  def removeBy(userId: UserId): Try[Int] = userRepository.removeBy(userId)
}
