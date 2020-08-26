package model

import lib.model.Todo._
// Topページのviewvalue
case class ViewValueTodoList(
  title:  String           = "Todoの一覧",
  cssSrc: Seq[String]      = Seq("todo.css"),
  jsSrc:  Seq[String]      = Seq("main.js"),
  data:   Seq[Todo]
) extends ViewValueCommon

