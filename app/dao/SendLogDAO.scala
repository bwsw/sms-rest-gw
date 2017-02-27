package dao

import javax.inject.{Inject, Singleton}

import models.SendLogRecord
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.lifted.{TableQuery, Tag}
import slick.driver.SQLiteDriver.api._
import org.joda.time.DateTime

import scala.concurrent.Future

/**
  * Created by Ruslan Komarov on 16.02.17.
  */
@Singleton
class SendLogDAO @Inject() (@NamedDatabase("storage") dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  implicit val dateColumnType = MappedColumnType.base[DateTime, Long](d => d.getMillis / 1000, d => new DateTime(d * 1000))

  private val sendLogs = TableQuery[SendLogsTable]

  def insert(sendLogRecord: SendLogRecord): Future[Unit] = dbConfig.db.run(sendLogs += sendLogRecord).map(_ => ())

  def selectByUserAndRange(username: String, from: DateTime, to: DateTime): Future[Seq[SendLogRecord]] = {
    val query = sendLogs.filter(_.username === username).filter(d => d.sendtime >= from && d.sendtime < to).result
    dbConfig.db.run(query)
  }

  private class SendLogsTable(tag: Tag) extends Table[SendLogRecord](tag, "SENDLOGS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def username = column[String]("USERNAME")
    def sender = column[String]("SENDER")
    def destination = column[String]("DESTINATION")
    def message = column[String]("MESSAGE")
    def sendtime = column[DateTime]("SENDTIME")

    def * = (username, sender, destination, message, sendtime) <> ((SendLogRecord.apply _).tupled, SendLogRecord.unapply)
  }
}
