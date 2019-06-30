package com.hack.services

import com.hack.daos.{GithubStatDao, VkStatDao}
import com.hack.models.{ServiceResponse, UserGithubStat, UserVKStat}
import org.bson.types.ObjectId
import org.mongodb.scala.bson.ObjectId

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait GithubService {
  def getAll(): Future[Either[ServiceResponse, Seq[UserGithubStat]]]
  def getSingleByUser(link: String): Future[Either[ServiceResponse, UserGithubStat]]
  def getSingleById(id: String): Future[Either[ServiceResponse, UserGithubStat]]
  def getByIdDirectly(id: ObjectId): Future[Option[UserGithubStat]]
  def create(link: String): Future[Either[ServiceResponse, UserGithubStat]]
}

class GithubServiceImpl(githubStatDao: GithubStatDao,
                        userService: UserService) extends GithubService {

  def getAll(): Future[Either[ServiceResponse, Seq[UserGithubStat]]] = {
    githubStatDao.getAll.map {
      case x: Seq[UserGithubStat] if x.nonEmpty => Right(x)
      case _ =>
        Left(ServiceResponse(false, Some("There are no stats")))
    }
  }

  def getSingleByUser(link: String): Future[Either[ServiceResponse, UserGithubStat]] = {
    if (ObjectId.isValid(link)) {
      val objectId = new ObjectId(link)
      userService.getByIdDirectly(objectId).flatMap {
        case Some(x) =>
          githubStatDao.getSingleByUser(link).map {
            case x if x != null => Right(x)
            case _ =>
              Left(ServiceResponse(false, Some("Error. This stat not found")))
          }
        case None =>
          Future.successful(
            Left(ServiceResponse(false, Some("Error. User not found"))))
      }
    } else Future.successful(
      Left(ServiceResponse(false, Some("Request doesn't correct!"))))
  }

  def getSingleById(id: String): Future[Either[ServiceResponse, UserGithubStat]] = {
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

  def getByIdDirectly(id: ObjectId): Future[Option[UserGithubStat]] = {
    githubStatDao.getSingleById(id).map { x =>
      Option(x)
    }
  }

  def create(link: String): Future[Either[ServiceResponse,
    UserGithubStat]] = {
    if (ObjectId.isValid(link)) {
      val objectId = new ObjectId(link)
      userService.getByIdDirectly(objectId).flatMap {
        case Some(x) =>
          githubStatDao.getSingleByUser(link).flatMap {
            case x if x != null =>
              val stat: UserGithubStat = UserGithubStat.apply(
                new ObjectId(), link)
              githubStatDao.create(stat).flatMap { x =>
                githubStatDao.getSingleById(stat._id).map { x => Right(x) }
              }
            case x => Future.successful(
              Left(ServiceResponse(false, Some("Cant create stat"))))
          }
        case None =>
          Future.successful(
            Left(ServiceResponse(false, Some("Error. This user not found"))))
      }
    } else Future.successful(
      Left(ServiceResponse(false, Some("Request doesn't correct!"))))
  }

}