package com.bwsw.dao

import javax.inject.{Inject, Singleton}

import com.bwsw.models.SendLogRecord
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.driver.SQLiteDriver.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Future

/**
  * Data Access Object for sent messages logs
  */
@Singleton
class SendLogDAO @Inject() (@NamedDatabase("storage") dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  implicit val dateColumnType = MappedColumnType.base[DateTime, Long](d => d.getMillis / 1000, d => new DateTime(d * 1000))

  private val sendLogs = TableQuery[SendLogsTable]

  /**
    * Insert SendLogRecord to database
    * @param sendLogRecord record to insert
    * @return future object
    */
  def insert(sendLogRecord: SendLogRecord): Future[Unit] = dbConfig.db.run(sendLogs += sendLogRecord).map(_ => ())

  /**
    * Select log records from database with filtering by parameters
    * @param username username filter
    * @param from from (time) filter
    * @param to to (time) filter
    * @return log records according to select parameters
    */
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
