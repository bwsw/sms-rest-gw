package dao

import java.util.Date
import javax.inject.{Inject, Singleton}

import models.UserToken
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
class UserTokenDAO @Inject()(@NamedDatabase("storage") dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  private val userTokens = TableQuery[UserTokensTable]

  def findByToken(token: String) : Future[Option[UserToken]] =  dbConfig.db.run(userTokens.filter(_.token === token).result.headOption)

  def insert(userToken: UserToken): Future[Unit] = dbConfig.db.run(userTokens += userToken).map(_ => ())

  implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

  private class UserTokensTable(tag: Tag) extends Table[UserToken](tag, "USERTOKENS") {
      def token = column[String]("TOKEN", O.PrimaryKey)
      def username = column[String]("USERNAME")
      def expirationTime = column[Date]("EXPIRATIONTIME")

      override def * = (token, username, expirationTime)  <> (UserToken.tupled, UserToken.unapply)
    }
}
