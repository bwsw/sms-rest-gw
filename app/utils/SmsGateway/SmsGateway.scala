package utils.SmsGateway

import models.Message

import scala.util.{Failure, Success, Try}

/**
  * Created by Ruslan Komarov on 17.02.17.
  */
trait SmsGateway {
  def sendMessage(message: Message): Either[String, String]
}

object SmsGateway {
  def getSmsGateway(): Option[SmsGateway] = {
    import com.typesafe.config.ConfigFactory
    Try(ConfigFactory.load().getString("app.gateway")) match {
      case Success(service) => service match {
        case "plivo" => Some(new PlivoSmsGateway)
        case _ => None
      }
      case Failure(_) => None
    }
  }
}
