package controllers

import javax.inject.Inject

import controllers.Secured.UserAction

import scala.concurrent.ExecutionContext.Implicits.global
import dao.SendLogDAO
import org.joda.time.DateTime
import play.api.mvc.Controller
import play.api.libs.json._

import scala.concurrent.Future

/**
  * Created by Ruslan Komarov on 20.02.17.
  */
class SendLogController @Inject() (sendLogDAO: SendLogDAO) extends Controller {
  implicit def long2datetime(v: Long) = new DateTime(v * 1000)

  def logs(from: Long, to: Long) = UserAction.async { request =>
    sendLogDAO.selectByUserAndRange(request.user.username, from, to) map {
      logsList => Ok(Json.obj("logs" -> Json.toJson(logsList))).as(JSON)
    }
  }
}
