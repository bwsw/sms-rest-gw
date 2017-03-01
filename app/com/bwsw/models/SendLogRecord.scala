package com.bwsw.models

import play.api.libs.json.Json

/**
  * Created by Ruslan Komarov on 16.02.17.
  */
case class SendLogRecord(username: String, sender: String, destination: String, message: String, sendtime: Long)

object SendLogRecord {
  implicit val sendLogRecordFormat = Json.format[SendLogRecord]
}
