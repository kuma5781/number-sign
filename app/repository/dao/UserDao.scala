package repository.dao

import java.sql.ResultSet

import play.api.db.DBApi
import repository.dao.DBAccessor.RichDBApi

import scala.util.Try

class UserDao(dbApi: DBApi) {

  case class UserDto(
      id: Int,
      name: String
  )

  case class NewUserDto(
      name: String
  )

  private val userDto = (rs: ResultSet) => {
    val userId = rs.getInt("user_id")
    val userName = rs.getString("user_name")
    UserDto(userId, userName)
  }

  def selectAll(): Try[Seq[UserDto]] = {
    val sql = "select * from user"
    dbApi.selectRecords(sql, userDto)
  }

  def selectBy(userId: Int): Try[UserDto] = {
    val sql = s"select * from user where user_id = $userId"
    dbApi.selectRecord(sql, userDto)
  }

  def insert(newUserDto: NewUserDto): Try[Int] = {
    val sql = s"insert into user (user_name) values (${newUserDto.name})"
    dbApi.insertRecord(sql)
  }
}
