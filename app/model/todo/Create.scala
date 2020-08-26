package model

import play.api.data.Form
import play.api.data.Forms._

import lib.model.Todo._
// Topページのviewvalue
case class ViewValueTodoCreateForm(
  title:  String           = "Todoの追加",
  cssSrc: Seq[String]      = Seq("todo.css"),
  jsSrc:  Seq[String]      = Seq("main.js"),
  form:   Form[FormValue],
) extends ViewValueCommon

case class ViewValueTodoCreate(
  title:  String      = "Todoの追加完了",
  cssSrc: Seq[String] = Seq("todo.css"),
  jsSrc:  Seq[String] = Seq("main.js"),
  data:   FormValue
) extends ViewValueCommon
