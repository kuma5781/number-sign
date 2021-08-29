package repository.dao

import java.sql.ResultSet

import scala.util.Try

class UserDao() {

  private val bdAccessor = new DBAccessor

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
    bdAccessor.selectRecords(sql, userDto)
  }

  def selectBy(userId: Int): Try[UserDto] = {
    val sql = s"select * from user where user_id = $userId"
    bdAccessor.selectRecord(sql, userDto)
  }

  def insert(newUserDto: NewUserDto): Try[Int] = {
    val sql = s"insert into user (user_name) values (${newUserDto.name})"
    bdAccessor.insertRecord(sql)
  }
}
