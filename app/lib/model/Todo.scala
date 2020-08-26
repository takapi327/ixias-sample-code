// パッケージにまとめて他で使えるようにする
package lib.model

import ixias.model._
import java.time.LocalDateTime

import Todo._
case class Todo (
  id:        Option[Id],
  title:     String,
  body:      String,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
)extends EntityModel[Id]

object Todo {
  
  val  Id = the[Identity[Id]]
  type Id = Long @@ Todo

  def build(title: String, body: String): WithNoId = {
    new Entity.WithNoId(
      new Todo(
        id    = None,
        title = title,
        body  = body
      )
    )
  }
  // Todo追加機能用
  case class FormValue (title: String, body: String)
}
