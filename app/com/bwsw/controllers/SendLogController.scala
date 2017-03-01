package com.bwsw.controllers

import javax.inject.Inject

import com.bwsw.controllers.Secured.UserAction
import com.bwsw.dao.SendLogDAO
import play.api.libs.json._
import play.api.mvc.Controller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

/**
  * Sent messages log controller
  */
class SendLogController @Inject() (sendLogDAO: SendLogDAO) extends Controller {
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
