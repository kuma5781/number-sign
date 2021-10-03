package domain.`object`.folder

import domain.`object`.user.UserId

case class Folder(
    id: FolderId,
    userId: UserId,
    name: FolderName,
    parentFolderId: Option[FolderId]
)

object Folder {
  def apply(folderDto: FolderDto): Folder =
    new Folder(
      FolderId(folderDto.id),
      UserId(folderDto.userId),
      FolderName(folderDto.name),
      folderDto.parentFolderId.map(FolderId)
    )

  case class FolderDto(
      id: Int,
      userId: Int,
      name: String,
      parentFolderId: Option[Int]
  )
}
