package models


import java.util.Date

/**
  * Created by Ruslan Komarov on 16.02.17.
  */
case class SendLogRecord(username: String, sender: String, destination: String, message: String, sendtime: Date)
