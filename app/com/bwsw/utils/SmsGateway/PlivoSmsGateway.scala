package com.bwsw.utils.SmsGateway

import java.util

import com.bwsw.models.Message
import com.plivo.helper.api.client.RestAPI
import com.typesafe.config.ConfigFactory
import play.api.http.Status


/**
  * Plivo implementation of SmsGateway service
  */
class PlivoSmsGateway extends SmsGateway {
  /**
    * Method implementation with usage of official Java Plivo library
    * @param message sent message
    * @return Left for errors while sending, Right for successful sending
    */
  override def sendMessage(message: Message): Either[String, String] = {
    val auth_id = ConfigFactory.load().getString("app.gateway.auth.id")
    val auth_token = ConfigFactory.load().getString("app.gateway.auth.token")

    val restAPI = new RestAPI(auth_id, auth_token, "v1")

    val params = new util.LinkedHashMap[String, String]
    params.put("src", message.sender)
    params.put("dst", message.destination)
    params.put("text", message.text)

    val response = restAPI.sendMessage(params)

    if (response.serverCode == Status.ACCEPTED) Right(response.message)
    else Left(response.error)
  }
}
