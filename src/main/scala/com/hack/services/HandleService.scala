package com.hack.services

import java.io.{BufferedWriter, File, FileWriter}

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.hack.models._
import net.liftweb.json.{JsonAST, _}
import org.bson.types.ObjectId
import org.mongodb.scala.bson.ObjectId
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model._
import com.hack.Server.system
import com.hack.Server.materializer
import com.typesafe.config.ConfigFactory
import org.json4s.jackson.Serialization.write
import akka.http.scaladsl.model.HttpRequest
import com.hack.configs.Json4sSupport._

import sys.process._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.Random

trait HandleService {
  def handleGithub(id: String): Future[Either[ServiceResponse,
                                              UserGithubStat]]
  def handleVK(id: String): Future[Either[ServiceResponse, UserVKStat]]
  def handleFacebook(id: String): Future[Either[ServiceResponse,
                                                UserFacebookStat]]
  def getVkData(link: String): Future[(List[String], List[String])]
}

class HandleServiceImpl(userService: UserService,
                        vkService: VkService) extends HandleService {

  def handleGithub(id: String): Future[Either[ServiceResponse,
    UserGithubStat]] = {
    ???
  }

  def getVkData(link: String): Future[(List[String], List[String])] = {
    val data = VkRequestFormat.apply(link)
    Http()
      .singleRequest(HttpRequest(
        HttpMethods.POST,
        Uri(s"http://10.20.2.66:8080/"),
        entity = HttpEntity(ContentTypes.`application/json`, write(data))
      )
      .withHeaders(
        RawHeader("Content-Type", "application/json"),
        RawHeader("Accept", "application/json")
      ))
      .flatMap(Unmarshal(_).to[Option[VkResponseFormat]])
      .map { x =>
        x match {
          case Some(x) => (x.interests, x.rates)
          case None => (List("sample"),List("sample"))
        }
      }
  }

  def handleVK(id: String): Future[Either[ServiceResponse, UserVKStat]] = {
    if (ObjectId.isValid(id)) {
      val objectId = new ObjectId(id)
      userService.getByIdDirectly(objectId).flatMap {
        case Some(x) =>
          x.vk_link match {
            case Some(y) =>
              getVkData(x.vk_link.get).flatMap { z =>
                vkService.create(id, z._1, z._2).map {
                  case Left(response) => Left(response)
                  case Right(stat) => Right(stat)
                }
              }
            case None =>
              Future.successful(
                Left(ServiceResponse(false, Some("User not found!"))))
          }
        case _ =>
          Future.successful(
            Left(ServiceResponse(false, Some("User not found!"))))
      }
    } else Future.successful(
      Left(ServiceResponse(false, Some("Request is not right"))))
  }

  def handleFacebook(id: String): Future[Either[ServiceResponse,
    UserFacebookStat]] = {
    //will release in the future
    ???
  }
}
