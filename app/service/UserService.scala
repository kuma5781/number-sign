package service

import domain.`object`.user.{ Email, NewUser, User, UserId, UserName }
import repository.UserRepository

import scala.util.{ Failure, Success, Try }

class UserService(userRepository: UserRepository = new UserRepository) {

  def findAll(): Try[Seq[User]] = userRepository.findAll()

  def findBy(userId: UserId): Try[User] = userRepository.findBy(userId)

  def findBy(email: Email): Try[User] =
    userRepository.findBy(email) match {
      case Success(user) => Success(user)
      case Failure(e) if e.toString == "java.lang.Exception: Not found record" =>
        val newUser = NewUser(UserName.generateFrom(email), email)
        for {
          userId <- userRepository.saveAndGetUserId(newUser)
          user <- userRepository.findBy(userId)
        } yield user
      case Failure(e) => Failure(e)
    }

  def save(newUser: NewUser): Try[Int] = userRepository.save(newUser)

  def updateName(userId: UserId, userName: UserName): Try[Int] = userRepository.updateName(userId, userName)

  def removeBy(userId: UserId): Try[Int] = userRepository.removeBy(userId)
}
