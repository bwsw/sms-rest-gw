package dao

import java.util.Date
import javax.inject.{Inject, Singleton}

import models.SendLogRecord
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.lifted.{TableQuery, Tag}
import slick.driver.SQLiteDriver.api._

import scala.concurrent.Future

/**
  * Created by Ruslan Komarov on 16.02.17.
  */
@Singleton
class SendLogDAO @Inject() (@NamedDatabase("storage") dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  private val sendLogs = TableQuery[SendLogsTable]

  def insert(sendLogRecord: SendLogRecord): Future[Unit] = dbConfig.db.run(sendLogs += sendLogRecord).map(_ => ())

  implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

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
