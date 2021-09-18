package repository.dao

import java.sql.ResultSet

import scala.util.Try

case class UserDto(
    id: Int,
    name: String
)

case class NewUserDto(
    name: String
)

class UserDao {

  private val tableName = "user"

  private val userDto = (rs: ResultSet) => {
    val userId = rs.getInt("id")
    val userName = rs.getString("name")
    UserDto(userId, userName)
  }

  def selectAll(): Try[Seq[UserDto]] = {
    val sql = s"select * from $tableName"
    DBAccessor.selectRecords(sql, userDto)
  }

  def selectBy(userId: Int): Try[UserDto] = {
    val sql = s"select * from $tableName where id = $userId"
    DBAccessor.selectRecord(sql, userDto)
  }

  def insert(newUserDto: NewUserDto): Try[Int] = {
    val sql = s"insert into $tableName (name) values ('${newUserDto.name}')"
    DBAccessor.execute(sql)
  }

  def update(userDto: UserDto): Try[Int] = {
    val sql = s"update $tableName set name = '${userDto.name}' where id = ${userDto.id}"
    DBAccessor.execute(sql)
  }

  def deleteBy(userId: Int): Try[Int] = {
    val sql = s"delete from $tableName where id = $userId"
    DBAccessor.execute(sql)
  }
}
