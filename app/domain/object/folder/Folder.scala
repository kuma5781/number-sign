package domain.`object`.folder

import domain.`object`.user.UserId

case class Folder(
    id: FolderId,
    userId: UserId,
    name: FolderName
)

object Folder {
  case class FolderDto(
      id: Int,
      userId: Int,
      name: String
  )
}
