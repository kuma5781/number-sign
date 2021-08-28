package service

import repository.UserRepository
import domain.`object`.user.User
import play.api.db.DBApi

class UserService(dbApi: DBApi) {

	private val userRepository = new UserRepository(dbApi)

	def findAll(): Seq[User] = userRepository.findAll()
}
