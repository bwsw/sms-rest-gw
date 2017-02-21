package controllers

import pdi.jwt.{JwtPlayImplicits, _}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.Future


/**
  * Created by Ruslan Komarov on 21.02.17.
  */

class AuthenticatedRequest[A](request: Request[A]) extends WrappedRequest[A](request)

trait Secured {
  def Authenticated = AuthenticatedAction
}

object AuthenticatedAction extends ActionBuilder[AuthenticatedRequest] with JwtPlayImplicits {
  def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]) = {
    request.jwtSession match {
      case JwtSession(_, claim, sign) if !sign.isEmpty &&
        claim.as[JwtClaim].isValid &&
        claim.as[JwtClaim].subject.isDefined => block(new AuthenticatedRequest(request )).map(_.refreshJwtSession(request))
      case _ => Future.successful(Unauthorized)
    }
  }
}