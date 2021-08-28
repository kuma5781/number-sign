package service

import domain.`object`.user.User
import repository.UserRepository

class UserService() {

  private val userRepository = new UserRepository

  def findAll(): Seq[User] = userRepository.findAll()
}
