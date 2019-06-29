package com.hack.services

import com.hack.daos.{AuthDao, UserDao}
import com.hack.models.{ServiceResponse, User}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AuthService {
  def auth(link: String): Future[Either[ServiceResponse, User]]
}

class AuthServiceImpl(dao: UserDao) extends AuthService {

  def auth(link: String): Future[Either[ServiceResponse, User]] = {
    dao.getSingleByLink(link).flatMap {
      case x if x != null => Future.successful(Right(x))
      case x => Future.successful(
        Left(ServiceResponse(false, Some("User ot found"))))
    }
  }

}
