package utils.SmsGateway
import java.util

import com.plivo.helper.api.client.RestAPI
import com.plivo.helper.api.response.message.MessageResponse
import com.typesafe.config.ConfigFactory
import models.Message

import scala.collection.mutable

/**
  * Created by Ruslan Komarov on 17.02.17.
  */
class PlivoSmsGateway extends SmsGateway {
  override def sendMessage(message: Message): String = {
    val auth_id = ConfigFactory.load().getString("plivo.auth_id")
    val auth_token = ConfigFactory.load().getString("plivo.auth_token")

    val restAPI = new RestAPI(auth_id, auth_token, "v1")

    val params = new util.LinkedHashMap[String, String]
    params.put("src", message.sender)
    params.put("dst", message.destination)
    params.put("text", message.text)

    restAPI.sendMessage(params).toString
  }
}
