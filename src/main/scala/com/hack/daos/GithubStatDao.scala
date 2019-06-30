package com.hack.daos

import com.hack.models.{ServiceResponse, User, UserGithubStat, UserVKStat}
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.{Completed, MongoCollection}
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.model.Filters.equal

import scala.concurrent.Future

trait GithubStatDao {
  def getAll: Future[Seq[UserGithubStat]]
  def getSingleByUser(link: String): Future[UserGithubStat]
  def getSingleById(id: ObjectId): Future[UserGithubStat]
  def create(stat: UserGithubStat): Future[Completed]
}

class GithubStatDaoImpl(
     githubCollection: MongoCollection[UserGithubStat]) extends GithubStatDao {

  def getAll =
    githubCollection.find().toFuture()

  def getSingleByUser(link: String): Future[UserGithubStat] = {
    githubCollection.find(equal("user_id", link))
      .first()
      .toFuture()
  }

  def getSingleById(id: ObjectId): Future[UserGithubStat] = {
    githubCollection.find(equal("_id", id))
      .first()
      .toFuture()
  }

  def create(stat: UserGithubStat): Future[Completed] =
    githubCollection.insertOne(stat).toFuture()
}


