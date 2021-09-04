package repository.dao

import java.sql.ResultSet

import scala.util.Try

class UserDao {

  case class UserDto(
      id: Int,
      name: String
  )

  case class NewUserDto(
      name: String
  )

  private val userDto = (rs: ResultSet) => {
    val userId = rs.getInt("id")
    val userName = rs.getString("name")
    UserDto(userId, userName)
  }

  def selectAll(): Try[Seq[UserDto]] = {
    val sql = "select * from user"
    DBAccessor.selectRecords(sql, userDto)
  }

  def selectBy(userId: Int): Try[UserDto] = {
    val sql = s"select * from user where id = $userId"
    DBAccessor.selectRecord(sql, userDto)
  }

  def insert(newUserDto: NewUserDto): Try[Int] = {
    val sql = s"insert into user (name) values (${newUserDto.name})"
    DBAccessor.insertRecord(sql)
  }
}
