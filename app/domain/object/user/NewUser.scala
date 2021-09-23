package domain.`object`.user

case class NewUser(
    name: UserName,
    email: Email
)

object NewUser {
  def apply(newUserDto: NewUserDto): NewUser = {
    NewUser(
      UserName(newUserDto.name),
      Email(newUserDto.email)
    )
  }

  case class NewUserDto(
      name: String,
      email: String
  )
}
