package com.bwsw.controllers

import javax.inject.Inject

import com.bwsw.controllers.Secured.UserAction
import com.bwsw.dao.SendLogDAO
import com.bwsw.models.{Message, SendLogRecord}
import com.bwsw.utils.SmsGateway.SmsGateway
import com.typesafe.config.ConfigException
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc.Controller

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * Sending messages controller
  */
class SendMessageController @Inject() (sendLogDAO: SendLogDAO) extends Controller {

  /**
    * Sending messages method.
    * Receive POST request (/send) with arguments: sender, destination and text.
    * Send messages using implementation of SmsGateway trait.
    * @return response with sending status or error with description
    */
  def send = UserAction.async(parse.json) { implicit request =>
    request.body.validate[Message] match {
      case _: JsError => Future.successful(BadRequest(Json.obj("error" -> "Invalid json")).as(JSON))
      case JsSuccess(m, _) => SmsGateway.getSmsGateway fold (
          error => Future.successful(BadRequest(Json.obj("error" -> error)).as(JSON)),
          service => Try(service.sendMessage(m)) match {
            case Success(result) => result.fold(
              error => Future.successful(BadRequest(Json.obj("error" -> error)).as(JSON)),
              result => {
                sendLogDAO.insert(SendLogRecord(request.user.username, m.sender, m.destination, m.text, DateTime.now()))
                Future.successful(Ok(Json.obj("message" -> result)).as(JSON))
              }
            )
            case Failure(e) => e match {
              case _: ConfigException => Future.successful(BadRequest(Json.obj("error" -> "Bad application config: missing SMS gateway setting(s))")).as(JSON))
              case _ => Future.successful(BadRequest(Json.obj("error" -> ("Unknown error: " + e.toString))).as(JSON))
            }
          }
      )
    }
  }
}
