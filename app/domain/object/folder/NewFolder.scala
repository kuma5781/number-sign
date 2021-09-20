package domain.`object`.folder

import domain.`object`.user.UserId

case class NewFolder(
    userId: UserId,
    name: FolderName,
    parentFolderId: Option[FolderId]
)

object NewFolder {
  def apply(newFolderDto: NewFolderDto): NewFolder =
    NewFolder(
      UserId(newFolderDto.userId),
      FolderName(newFolderDto.name),
      newFolderDto.parentFolderId.map(FolderId)
    )

  case class NewFolderDto(
      userId: Int,
      name: String,
      parentFolderId: Option[Int]
  )
}
