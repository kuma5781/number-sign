package domain.`object`.user

case class User(
    id: UserId,
    name: UserName,
    email: Email
)

object User {
  def apply(userDto: UserDto): User = {
    User(
      UserId(userDto.id),
      UserName(userDto.name),
      Email(userDto.email)
    )
  }

  case class UserDto(
      id: Int,
      name: String,
      email: String
  )
}
