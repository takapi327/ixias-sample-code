/**
  * This is a sample of Todo Application.
  * 
  */

package lib.persistence.db

import java.time.LocalDateTime
import slick.jdbc.JdbcProfile
import ixias.persistence.model.Table

import lib.model.Todo

// UserTable: Userテーブルへのマッピングを行う
//~~~~~~~~~~~~~~
case class TodoTable[P <: JdbcProfile]()(implicit val driver: P)
  extends Table[Todo, P] {
  import api._

  // Definition of DataSourceName
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  lazy val dsn = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/ixias_sample_code"),
    "slave"  -> DataSourceName("ixias.db.mysql://slave/ixias_sample_code")
  )

  // Definition of Query
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  class Query extends BasicQuery(new Table(_)) {}
  lazy val query = new Query

  // Definition of Table
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  class Table(tag: Tag) extends BasicTable(tag, "todo") {
    import Todo._
    // Columns
    /* @1 */ def id        = column[Id]            ("id",         O.UInt64, O.PrimaryKey, O.AutoInc)
    /* @2 */ def title     = column[String]        ("title",      O.Utf8Char255)
    /* @3 */ def body      = column[String]        ("body",       O.Utf8Char255)
    /* @4 */ def updatedAt = column[LocalDateTime] ("updated_at", O.TsCurrent)
    /* @5 */ def createdAt = column[LocalDateTime] ("created_at", O.Ts)

    type TableElementTuple = (
      Option[Id], String, String, LocalDateTime, LocalDateTime
    )

    // DB <=> Scala の相互のmapping定義
    def * = (id.?, title, body, updatedAt, createdAt) <> (
      // Tuple(table) => Model
      (t: TableElementTuple) => Todo(
        t._1, t._2, t._3, t._4, t._5
      ),
      // Model => Tuple(table)
      (v: TableElementType) => Todo.unapply(v).map { t => (
        t._1, t._2, t._3, LocalDateTime.now(), t._5
      )}
    )
  }
}
