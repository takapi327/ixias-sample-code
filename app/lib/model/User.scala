package lib.model

import ixias.model._
import java.time.LocalDateTime

// ケースクラス
import User._
case class User(
  id:        Option[Id],
  name:      String,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

// コンパニオンオブジェクト
object User {
  val Id  = the[Identity[Id]]
  type Id = Long @@ User
 
  def build(name: String): User#WithNoId =
    new User(
      id   = None,
      name = name,
    ).toWithNoId
}
