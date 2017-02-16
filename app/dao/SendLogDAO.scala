package dao

import java.util.Date
import javax.inject.{Inject, Singleton}

import models.SendLogRecord
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.lifted.{TableQuery, Tag}
import slick.driver.SQLiteDriver.api._

import scala.concurrent.Future

/**
  * Created by Ruslan Komarov on 16.02.17.
  */
@Singleton
class SendLogDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  private val sendLogs = TableQuery[SendLogsTable]

  def insert(sendLogRecord: SendLogRecord): Future[Unit] = db.run(sendLogs += sendLogRecord).map(_ => ())

  private class SendLogsTable(tag: Tag) extends Table[SendLogRecord](tag, "SENDLOGS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def username = column[String]("USERNAME")
    def sender = column[String]("SENDER")
    def destination = column[String]("DESTINATION")
    def message = column[String]("MESSAGE")
    def sendtime = column[Date]("SENDTIME")

    def * = (username, sender, destination, message, sendtime) <> (SendLogRecord.tupled, SendLogRecord.unapply)
  }
}
