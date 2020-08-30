 /**
  * This is a sample of CRUD Action.
  * 
  **/

package lib.persistence

import lib.model.Todo  // ixias.modelで定義したものをimport
import scala.concurrent.Future
import slick.jdbc.JdbcProfile
import ixias.persistence.SlickRepository

/**
 *
 * UserRepository: UserTableへのクエリ発行を行うRepository層の定義
 * extendsで継承しているものは、IxiaSの定義でRunDBActionなどを使用できるようにするもの
 * 詳しくはブログ参照
 * https://medium.com/nextbeat-engineering/%E8%87%AA%E7%A4%BEoss-ixias-%E3%81%AE%E7%B4%B9%E4%BB%8B-ixias-persistence%E3%83%91%E3%83%83%E3%82%B1%E3%83%BC%E3%82%B8%E3%81%AE%E3%82%B5%E3%83%B3%E3%83%97%E3%83%AB%E3%82%B3%E3%83%BC%E3%83%89-f1b6965fb1d6
 *
 * db.SlickResourceProviderは、リポジトリ内で作成したTableを使用できるようにするもの
 */
//~~~~~~~~~~~~~~~~~~~~~~
case class TodoRepository[P <: JdbcProfile]()(implicit val driver: P)
  extends SlickRepository[Todo.Id, Todo, P]
  with db.SlickResourceProvider[P] {

  import api._

  /**
    * Get Todo Data
    */
  def get(id: Id): Future[Option[EntityEmbeddedId]] =
    /**
     * (TodoTable)はIxiaSの処理
     * DBの読み込み、検索、取得は"Slave"を設定
     * 戻り値がEntityEmbeddedIdになっているのが、RunDBActionの恩恵です。
     * 本来Slickのdb.runではdefaultでInt型(DBに保存できた個数？)が帰ってきており、
     * 保存したUser型を取得するにはさらに別の処理を記載する必要があります。
     * ただRunDBActionでは、DefaultでEntityモデルを返すので、ユーザーはそのまま何も考えずに処理ができる分けです。
     * データベースに値が保存されたということはIdが必ずあることが担保されています。
     * なのでIdをOption型にする必要はなく、EntityEmbeddedIdの型として返しています
     * このおかげでOptionのIdに対してgetOrelse(0)みたいな不格好な処理をしなくてすみます。
     */
    RunDBAction(TodoTable, "slave") { _
      .filter(_.id === id)
      .result.headOption
  }

  /**
    * Add User Data
   */
  def add(entity: EntityWithNoId): Future[Id] =
    /**
     * RunDBAction(TodoTable)はIxiaSの処理
     * Slickの処理をラッピングしてSlickのdb.runのような処理をしている。
     * 引数にテーブルと"slave"を指定可能。defaultではmaster
     * DBの値に何か処理を行う場合はmaster
     * これはDBのレプリケーションの設定を行っている
     * 最初はRunDBActionでテーブルを指定して行いたい処理を書くぐらいの認識で大丈夫だと思います。
     */
    RunDBAction(TodoTable) { slick =>
      /**
       *
       * ここはSlickの処理
       * returningで戻り値をidに設定
       * +=で引数の値をINSERTする処理をしている
       */
      slick returning slick.map(_.id) += entity.v
    }

  /**
   * Update Todo Data
   * updateメソッドにEntityモデルを渡し、RunDEActionでTodoTableに処理を行う
   * filterで引数のEntityモデルのIdがDBに存在するかを検証
   * 存在していた場合はSQLクエリのupdateを行うことで処理が完成する
   */
  def update(entity: EntityEmbeddedId): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === entity.id)
      for {
        old <- row.result.headOption
        _   <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.update(entity.v)
        }
      } yield old
    }

  /**
   * Delete Todo Data
   * 上記updateと処理は同じ、違いは存在していた場合にSQLクエリのdeleteで該当データを削除する
   */
  def remove(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _   <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.delete
        }
      } yield old
    }
}

