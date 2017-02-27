package com.bwsw.controllers

import javax.inject.Inject

import com.bwsw.controllers.Secured.UserAction
import com.bwsw.dao.SendLogDAO
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Sent messages log controller
  */
class SendLogController @Inject() (sendLogDAO: SendLogDAO) extends Controller {
  implicit def long2datetime(v: Long) = new DateTime(v * 1000)

  /**
    * Method receive GET request (/logs) with "from" and "to" and return sent messages log in a particular time interval.
    * @param from start of interval
    * @param to end of interval
    * @return response with a list of logs
    */
  def logs(from: Long, to: Long) = UserAction.async { request =>
    sendLogDAO.selectByUserAndRange(request.user.username, from, to) map {
      logsList => Ok(Json.obj("logs" -> Json.toJson(logsList))).as(JSON)
    }
  }
}
