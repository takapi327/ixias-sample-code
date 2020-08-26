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
class TodoUpdateController @Inject()(
  val controllerComponents: ControllerComponents
) extends BaseController {

  def showUpdateForm(id: Long) = Action async {implicit req =>
    for {
      optionTodo <- TodoRepository.getById(Todo.Id(id))
    } yield {
      optionTodo match {
        case None => {
          val vv = ViewValueError(
            title   =  "指定したTodoは存在しません"
          )
          NotFound(views.html.error.NotFound404page(vv))
        }
        case Some(todo) => {
          val updateFormValue: Form[Todo.FormValue] = StatusValue.todoForm.fill(
           Todo.FormValue(
             todo.v.title,
             todo.v.body
           )
          )
          val vv = ViewValueTodoedit(
           form = updateFormValue,
         )
         Ok(views.html.site.todo.ShowUpdateForm(vv))
        }
      }
      val vv = ViewValueTodolist(
        data = todoSeq
      )
      Ok(views.html.site.todo.List(vv))
    }
  }

  def update(id: Long) = Action async {implicit req =>
    StatusValue.todoForm.bindFromRequest.fold(
      errorform => {
        val vv: ViewValueTodoUpdateForm = ViewValueTodoUpdateForm(
          form = errorform
        )
        BadRequest(views.html.site.todo.UpdateForm(vv))
      },
      successform => {
        val newEntityWithNoId = Todo.build(
          successform.title,
          successform.body
        )
        for {
          oldOptionEntity <- TodoRepository.getById(id)
          Some(newEntity) = oldOptionEntity.flatMap(v => v.copy(
           title  = successform.title,
           body   = successform.body,
          ))
          _ <- TodoRepository.update(newEntity)
        } yield {
          val vv: ViewValueTodoUpdate = ViewValueTodoUpdate(
            data = successform
          )
          Ok(views.html.site.todo.UpdateComplete(vv))
        }
      }
    )
  }
}

