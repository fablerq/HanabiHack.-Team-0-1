package com.hack.services

import com.hack.daos.{UserDao}
import com.hack.models.{ServiceResponse, User}
import org.bson.types.ObjectId

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.mongodb.scala.bson.ObjectId

trait UserService {
  def getAll(): Future[Either[ServiceResponse, Seq[User]]]
  def getSingleByLink(link: String): Future[Either[ServiceResponse, User]]
  def getSingleById(id: String): Future[Either[ServiceResponse, User]]
  def getByIdDirectly(id: ObjectId): Future[Option[User]]
  def create(name: String): Future[Either[ServiceResponse, User]]
  def updateGithub(id: String, link: String): Future[ServiceResponse]
  def updateVK(id: String, link: String): Future[ServiceResponse]
  def updateFacebook(id: String, link: String): Future[ServiceResponse]
}

class UserServiceImpl(dao: UserDao) extends UserService {

  def getAll(): Future[Either[ServiceResponse, Seq[User]]] = {
    dao.getAll.map {
      case x: Seq[User] if x.nonEmpty => Right(x)
      case _ =>
        Left(ServiceResponse(false, Some("There are no users")))
    }
  }

  def getSingleByLink(link: String): Future[Either[ServiceResponse, User]] = {
    dao.getSingleByLink(link).map {
      case x if x != null => Right(x)
      case _ =>
        Left(ServiceResponse(false, Some("Error. This user not found")))
    }
  }

  def getSingleById(id: String): Future[Either[ServiceResponse, User]] = {
    if (ObjectId.isValid(id)) {
      val objectId = new ObjectId(id)
      getByIdDirectly(objectId).flatMap {
        case Some(x) => Future.successful(Right(x))
        case _ =>
          Future.successful(
            Left(ServiceResponse(false, Some("User not found!"))))
      }
    } else Future.successful(
        Left(ServiceResponse(false, Some("Request not right"))))
  }

  def getByIdDirectly(id: ObjectId): Future[Option[User]] = {
    dao.getSingleById(id).map { x =>
      Option(x)
    }
  }

  def create(link: String): Future[Either[ServiceResponse, User]] = {
    dao.getSingleByLink(link).flatMap {
      case x if x == null =>
        val user: User = User.apply(
          new ObjectId(), "Аноним", Some(link), None, None)
        dao.create(user).flatMap { x =>
          dao.getSingleById(user._id).map { x => Right(x) }
        }
      case x => Future.successful(
                  Left(ServiceResponse(false, Some("Cant create user"))))
    }
  }

  def updateGithub(id: String, link: String): Future[ServiceResponse] = {
    if (ObjectId.isValid(id)) {
      val objectId = new ObjectId(id)
      getByIdDirectly(objectId).flatMap {
        case Some(x) =>
          dao.updateGithub(objectId, link).map { x=>
            ServiceResponse(true, Some("Github successfully added"))
          }
        case None =>
          Future.successful(
            ServiceResponse(false, Some("User not found")))
      }
    } else Future.successful(
            ServiceResponse(false, Some("Request doesn't correct!")))
  }


  def updateVK(id: String, link: String): Future[ServiceResponse] = {
    if (ObjectId.isValid(id)) {
      val objectId = new ObjectId(id)
      getByIdDirectly(objectId).flatMap {
        case Some(x) =>
          dao.updateVK(objectId, link).map { x=>
            ServiceResponse(true, Some("VK successfully added"))
          }
        case None =>
          Future.successful(
            ServiceResponse(false, Some("User not found")))
      }
    } else Future.successful(
      ServiceResponse(false, Some("Request doesn't correct!")))
  }


  def updateFacebook(id: String, link: String): Future[ServiceResponse] = {
    if (ObjectId.isValid(id)) {
      val objectId = new ObjectId(id)
      getByIdDirectly(objectId).flatMap {
        case Some(x) =>
          dao.updateFacebook(objectId, link).map { x=>
            ServiceResponse(true, Some("Facebook successfully added"))
          }
        case None =>
          Future.successful(
            ServiceResponse(false, Some("User not found")))
      }
    } else Future.successful(
      ServiceResponse(false, Some("Request doesn't correct!")))
  }

}
