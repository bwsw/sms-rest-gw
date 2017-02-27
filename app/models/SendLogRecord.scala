package models


import org.joda.time.DateTime
import play.api.libs.json.Json

/**
  * Created by Ruslan Komarov on 16.02.17.
  */
case class SendLogRecord(username: String, sender: String, destination: String, message: String, sendtime: DateTime)

object SendLogRecord {
  implicit val sendLogRecordFormat = Json.format[SendLogRecord]
}
