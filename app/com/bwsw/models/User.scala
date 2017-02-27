package com.bwsw.models

import play.api.libs.json.Json


/**
  * User model class
  * @param username user's username
  */
case class User(username: String)

/**
  * Object-companion with implicit method for JSON formatting
  */
object User {
  implicit val userFormat = Json.format[User]
}