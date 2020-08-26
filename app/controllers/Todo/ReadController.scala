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
class TodoReadController @Inject()(
  val controllerComponents: ControllerComponents
) extends BaseController {

  def showGetAll() = Action async {implicit req =>
    for {
      todoSeq <- TodoRepository.get()
    } yield {
      val vv = ViewValueTodolist(
        data = todoSeq
      )
      Ok(views.html.site.todo.List(vv))
    }
  }
}
