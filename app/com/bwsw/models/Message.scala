package com.bwsw.models

import play.api.libs.json.Json

/**
  * Message model class
  * @param sender sender message
  * @param destination message's destination
  * @param text message text
  */
case class Message(sender: String, destination: String, text: String)

/**
  * Object-companion with implicit method for JSON formatting
  */
object Message {
  implicit val messageFormat = Json.format[Message]
}
