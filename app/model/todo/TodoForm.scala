package model

import play.api.data.Form
import play.api.data.Forms._
import lib.model._

object StatusValue {
  val todoForm: Form[Todo.FormValue] = Form (
    mapping(
      "title" -> nonEmptyText,
      "body"  -> nonEmptyText,
    )(Todo.FormValue.apply)(Todo.FormValue.unapply)
  )
}
