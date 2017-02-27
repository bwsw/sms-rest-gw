package com.bwsw.controllers

import com.bwsw.models.User
import pdi.jwt.JwtPlayImplicits
import play.api.http.ContentTypes._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.Future


/**
  * Secured actions object
  */
object Secured extends JwtPlayImplicits {
  class AuthenticatedRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)

  /**
    * Check user action: get token from header and try to decode it.
    * If success: redirect request into target action,
    * otherwise return Unauthorized with description
    */
  object UserAction extends ActionBuilder[AuthenticatedRequest] {
    def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {
      request.jwtSession.getAs[User]("user") match {
        case Some(user) => block(new AuthenticatedRequest(user, request)).map(_.refreshJwtSession(request))
        case _ => Future.successful(Unauthorized(Json.obj("error" -> "Bad or expired token")).as(JSON))
      }
    }
  }
}