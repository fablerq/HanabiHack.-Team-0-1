package com.hack.services

import com.hack.daos.{UserDao, VkStatDao}
import com.hack.models.{ServiceResponse, User, UserVKStat}
import org.bson.types.ObjectId

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.mongodb.scala.bson.ObjectId

trait VkService {
  def getAll(): Future[Either[ServiceResponse, Seq[UserVKStat]]]
  def getSingleByUser(link: String): Future[Either[ServiceResponse, UserVKStat]]
  def getSingleById(id: String): Future[Either[ServiceResponse, UserVKStat]]
  def getByIdDirectly(id: ObjectId): Future[Option[UserVKStat]]
  def create(link: String,
             interests: List[String],
             rates: List[String]): Future[Either[ServiceResponse,
                                                 UserVKStat]]
}

class VkServiceImpl(vkStatDao: VkStatDao,
                    userService: UserService) extends VkService {

  def getAll(): Future[Either[ServiceResponse, Seq[UserVKStat]]] = {
    vkStatDao.getAll.map {
      case x: Seq[UserVKStat] if x.nonEmpty => Right(x)
      case _ =>
        Left(ServiceResponse(false, Some("There are no stats")))
    }
  }

  def getSingleByUser(link: String): Future[Either[ServiceResponse, UserVKStat]] = {
    if (ObjectId.isValid(link)) {
      val objectId = new ObjectId(link)
      userService.getByIdDirectly(objectId).flatMap {
        case Some(x) =>
          vkStatDao.getSingleByUser(link).map {
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

  def getSingleById(id: String): Future[Either[ServiceResponse, UserVKStat]] = {
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

  def getByIdDirectly(id: ObjectId): Future[Option[UserVKStat]] = {
    vkStatDao.getSingleById(id).map { x =>
      Option(x)
    }
  }

  def create(link: String,
             interests: List[String],
             rates: List[String]): Future[Either[ServiceResponse,
                                                 UserVKStat]] = {
    if (ObjectId.isValid(link)) {
      val objectId = new ObjectId(link)
      userService.getByIdDirectly(objectId).flatMap {
        case Some(x) =>
          vkStatDao.getSingleByUser(link).flatMap {
            case x if x != null =>
              val stat: UserVKStat = UserVKStat.apply(
                new ObjectId(), link, interests, rates)
              vkStatDao.create(stat).flatMap { x =>
                vkStatDao.getSingleById(stat._id).map { x => Right(x) }
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
