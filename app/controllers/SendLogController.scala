package controllers

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import dao.SendLogDAO
import org.joda.time.DateTime
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

/**
  * Created by Ruslan Komarov on 20.02.17.
  */
class SendLogController @Inject() (sendLogDAO: SendLogDAO) extends Controller with Secured {
  implicit def long2datetime(v: Long) = new DateTime(v * 1000)

  def logs(from: Long, to: Long) = Authenticated.async { request =>
    sendLogDAO.selectByUserAndRange("user", from, to).map {res => Ok("ok: " + res.toString())}
  }
}
