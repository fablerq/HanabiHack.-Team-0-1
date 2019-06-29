package com.hack.services

import java.io.{BufferedWriter, File, FileWriter}

import com.hack.models._
import net.liftweb.json.{JsonAST, _}
import org.bson.types.ObjectId
import org.mongodb.scala.bson.ObjectId

import sys.process._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.Random

trait HandleService {
  def handleGithub(id: String): Future[Either[ServiceResponse,
                                              UserGithubStatModel]]
  def handleVK(id: String): Future[Either[ServiceResponse, UserVKStat]]
  def handleFacebook(id: String): Future[Either[ServiceResponse,
                                                UserFacebookStat]]
}

class HandleServiceImpl(authService: AuthService,
                        userService: UserService) extends HandleService {

  def handleGithub(id: String): Future[Either[ServiceResponse,
    UserGithubStatModel]] = {
    ???
  }

  def handleVK(id: String): Future[Either[ServiceResponse, UserVKStat]] = {
    ???
  }

  def handleFacebook(id: String): Future[Either[ServiceResponse,
    UserFacebookStat]] = {
    //will release in the future
    ???
  }
}
