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
class TodoController @Inject()(
  val controllerComponents: ControllerComponents
) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def showCreateForm() = Action async { implicit req =>
    val vv: ViewValueTodoCreateForm = ViewValueTodoCreateForm(
      form = StatusValue.todoForm
    )
    Ok(views.html.site.todo.CreateForm(vv))
  }

  def create() = Action async {implicit req =>
    StatusValue.todoForm.bindFromRequest.fold(
      errorform => {
        val vv: ViewValueTodoCreateForm = ViewValueTodoCreateForm(
          form = errorform
        )
        BadRequest(views.html.site.todo.CreateForm(vv))
      },
      successform => {
        val newEntityWithNoId = Todo.build(
          successform.title,
          successform.body
        )
        for {
          _ <- TodoRepository.add(newEntityWithNoId)
        } yield {
          val vv: ViewValueTodoCreate = ViewValueTodoCreate(
            data = successform
          )
          Ok(views.html.site.todo.CreateComplete(vv))
        }
      }
    )
  }
}

