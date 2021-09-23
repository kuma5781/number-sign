package domain.`object`.note

sealed abstract class NoteStatus(val value: String)

object NoteStatus {
  case object Active extends NoteStatus("active")
  case object Trashed extends NoteStatus("trashed")

  private val values: Seq[NoteStatus] = Seq(Active, Trashed)

  def apply(value: String): Option[NoteStatus] = values.find(_.value == value)
}
