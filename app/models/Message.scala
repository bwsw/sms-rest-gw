package models

import play.api.libs.json.Json

/**
  * Created by Ruslan Komarov on 17.02.17.
  */
case class Message(sender: String, destination: String, text: String)

object Message {
  implicit val messageFormat = Json.format[Message]
}
