package com.hack.daos

import com.hack.models.{ServiceResponse, User, UserVKStat}
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.{Completed, MongoCollection}
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.model.Filters.equal

import scala.concurrent.Future

trait VkStatDao {
  def getAll: Future[Seq[UserVKStat]]
  def getSingleByUser(link: String): Future[UserVKStat]
  def getSingleById(id: ObjectId): Future[UserVKStat]
  def create(stat: UserVKStat): Future[Completed]
}

class VkStatDaoImpl(
         vkCollection: MongoCollection[UserVKStat]) extends VkStatDao {

  def getAll =
    vkCollection.find().toFuture()

  def getSingleByUser(link: String): Future[UserVKStat] = {
    vkCollection.find(equal("user_id", link))
      .first()
      .toFuture()
  }

  def getSingleById(id: ObjectId): Future[UserVKStat] = {
    vkCollection.find(equal("_id", id))
      .first()
      .toFuture()
  }

  def create(stat: UserVKStat): Future[Completed] =
    vkCollection.insertOne(stat).toFuture()

}


