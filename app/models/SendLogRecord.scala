package models


import org.joda.time.DateTime

/**
  * Created by Ruslan Komarov on 16.02.17.
  */
case class SendLogRecord(username: String, sender: String, destination: String, message: String, sendtime: DateTime)
