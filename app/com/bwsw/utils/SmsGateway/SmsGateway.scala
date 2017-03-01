package com.bwsw.utils.SmsGateway

import com.bwsw.models.Message

import scala.util.Try

/**
  * SMS Gateway trait
  */
trait SmsGateway {
  /**
    * Trait provides method for sending messages using trait's implementations
    * @param message sent message
    * @return Left for errors while sending, Right for successful sending
    */
  def sendMessage(message: Message): Either[String, String]
}

/**
  * Object-companion for creating SmsGateway's implementation instance using configuration file
  */
object SmsGateway {
  /**
    * Create an instance of SmsGateway's implementation
    * @return Left for errors while creating, Right for return instance of gateway
    */
  def getSmsGateway(): Either[String, SmsGateway] = {
    import com.typesafe.config.ConfigFactory

    Try(ConfigFactory.load().getString("app.gateway.service")) map {
      serviceName =>
        Try(Class.forName(serviceName).newInstance().asInstanceOf[SmsGateway]) map {
          instance => Right(instance)
        } getOrElse Left("Bad application config: incorrect [app.gateway.service] setting")
    } getOrElse {Left("Bad application config: missing [app.gateway.service] setting")}
  }
}
