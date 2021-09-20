package domain.`object`.folder

case class RelayFolders(
    folderId: FolderId,
    parentFolderId: FolderId
)

object RelayFolders {
  def apply(relayFoldersDto: RelayFoldersDto): RelayFolders =
    RelayFolders(
      FolderId(relayFoldersDto.folderId),
      FolderId(relayFoldersDto.parentFolderId)
    )

  case class RelayFoldersDto(
      folderId: Int,
      parentFolderId: Int
  )
}
