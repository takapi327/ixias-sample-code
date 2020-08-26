package controllers.todo

import javax.inject._
import play.api._
import play.api.mvc._

import lib.model.Todo
import lib.persistence.default.TodoRepository
import model._
import java.awt.Desktop.Action

import play.api.data.Form
import play.api.data.Forms._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class TodoCreateController @Inject()(
  val controllerComponents: ControllerComponents
) extends BaseController {

  /**
   * Todo登録用のフォームを表示するためのAction
   * routesファイルに記載されているAPI
   * 例: localhost:4200/todo/show/createform 
   *
   * @param vv ViewValue -> Twirlにまとめてデータを送るためのモデル
   * case class で型付けすることもでき型の不一致の場合コンパイラで落としてくれる
   *
   * case class ViewValueTodoCreateForm(
   *   title:  String           = "Todoの追加",
   *   cssSrc: Seq[String]      = Seq("todo.css"),
   *   jsSrc:  Seq[String]      = Seq("main.js"),
   *   form:   Form[FormValue],
   * ) extends ViewValueCommon
   *
   * もしViewValueを使用しない場合4つの値をバラバラでViewに渡さなければいけない。
   * 個別処理がめんどくさいし、冗長なコードも増える
   * 不用意なエラーが出てしまう可能性が高まる
   */
  def showCreateForm() = Action async { implicit req =>
    val vv: ViewValueTodoCreateForm = ViewValueTodoCreateForm(
      form = StatusValue.todoForm
    )
    Ok(views.html.site.todo.CreateForm(vv))
  }

  /**
   * Todo登録処理をするためのAction
   * routesファイルに記載されているAPI
   * 例: localhost:4200/todo/complete/create
   *
   */  
  def create() = Action async {implicit req =>
    /**
     *
     * @param StatusValue フォームを作成しているobject
     * @Type  Form
     * object StatusValue {
     *  val todoForm: Form[Todo.FormValue] = Form (
     *     mapping(
     *       "title" -> nonEmptyText,
     *       "body"  -> nonEmptyText,
     *     )(Todo.FormValue.apply)(Todo.FormValue.unapply)
     *  )
     * }
     *
     * このアクション内で登録用フォームで作成したフォームが使用できるのは、
     * Viewでcreateメソッドを指定してリクエストを送ることで、ActionのRequestHeaderに渡されてimplicit reqでhogehoge
     * して使用可能となっている。ActionBuilderとかを見てみるとわかりやすい(まだ完全に理解していないので間違っているかもです)
     *
     * 渡された値に対してFormで定義されているbindFromRequest.foldで処理分けをしている。
     * 簡単にいうとOption[T]のSomeとNone、EitherのRightとLeftみたいな感じです。
     */
    StatusValue.todoForm.bindFromRequest.fold(
      /**
       * @param errorform 失敗した処理
       * FormValue型で失敗はしているが入力して値自体は入っている
       * なので今回はもう一度ViewValueを作成しBadRequestとしてもう一度登録フォームに飛ばしている。
       *
       * formにerrorformを渡すことで先ほど入力された状態にままのFormが表示される。
       * 例えばここで表示させる文字を変えてエラー文を渡してViewに表示させるとかの処理もできます。
       */
      errorform => {
        val vv: ViewValueTodoCreateForm = ViewValueTodoCreateForm(
          form = errorform
        )
        BadRequest(views.html.site.todo.CreateForm(vv))
      },

      /**
       * @param successform 成功した処理
       * 処理が成功しているので、successformの値を使いTodoモデルを作成します
       * モデル側で使用したbuildメソッドで変数名通りwithNoId型のEntityモデルを作成
       * Idはデータベースに保存する時に自動で生成してくれるので不要
       */
      successform => {
        val newEntityWithNoId = Todo.build(
          successform.title,
          successform.body
        )
      /**
       * リポジトリーのaddメソッドで先ほど作成したTodoモデルを私登録処理を行わせる
       * _(アンダーバー)で処理をしているのは、変数化してもその後処理を行わないので、このようにしている
       * 変数化していると何か処理を行ってしまう可能性が出てきてしまう。アンスコだとその心配がない
       *
       */
        for {
          _ <- TodoRepository.add(newEntityWithNoId)
        } yield {
          // 今まで通りの処理
          val vv: ViewValueTodoCreate = ViewValueTodoCreate(
            data = successform
          )
          Ok(views.html.site.todo.CreateComplete(vv))
        }
      }
    )
  }
}

