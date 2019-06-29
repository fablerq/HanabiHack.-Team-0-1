package com.hack.daos

import com.hack.models.{ServiceResponse, User}
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.{Completed, MongoCollection}
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.model.Filters.equal

import scala.concurrent.Future

trait UserDao {
  def getAll: Future[Seq[User]]
  def getSingleByLink(link: String): Future[User]
  def getSingleById(id: ObjectId): Future[User]
  def create(user: User): Future[Completed]
  def updateGithub(id: ObjectId, link: String): Future[UpdateResult]
  def updateVK(id: ObjectId, link: String): Future[UpdateResult]
  def updateFacebook(id: ObjectId, link: String): Future[UpdateResult]
}

class UserDaoImpl(userCollection: MongoCollection[User]) extends UserDao {

  def getAll =
    userCollection.find().toFuture()

  def getSingleByLink(link: String): Future[User] = {
    userCollection.find(equal("github_link", link))
      .first()
      .toFuture()
  }

  def getSingleById(id: ObjectId): Future[User] = {
    userCollection.find(equal("_id", id))
      .first()
      .toFuture()
  }

  def create(user: User): Future[Completed] =
    userCollection.insertOne(user).toFuture()

  def updateGithub(id: ObjectId, link: String): Future[UpdateResult] = {
    userCollection.updateOne(
      Document("_id" -> id),
      Document("$set" -> Document("github_link" -> link))
    ).toFuture()
  }

  def updateVK(id: ObjectId, link: String): Future[UpdateResult] = {
    userCollection.updateOne(
      Document("_id" -> id),
      Document("$set" -> Document("vk_link" -> link))
    ).toFuture()
  }

  def updateFacebook(id: ObjectId, link: String): Future[UpdateResult] = {
    userCollection.updateOne(
      Document("_id" -> id),
      Document("$set" -> Document("facebook_link" -> link))
    ).toFuture()
  }

}


