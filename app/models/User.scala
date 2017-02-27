package models

import play.api.libs.json._


/**
  * Created by Ruslan Komarov on 22.02.17.
  */
case class User(username: String)

object User {
  implicit val userFormat = Json.format[User]
}