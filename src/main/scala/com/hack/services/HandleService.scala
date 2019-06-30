package com.hack.services

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import net.liftweb.json.{JsonAST, _}

import sys.process._
import scala.io.Source
import java.nio.file.{Files, Paths}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.hack.Server.system
import com.hack.Server.materializer

import java.io.{BufferedWriter, File, FileWriter}

import com.hack.models._
import org.bson.types.ObjectId
import org.mongodb.scala.bson.ObjectId
import com.typesafe.config.ConfigFactory
import org.json4s.jackson.Serialization.write
import com.hack.configs.Json4sSupport._

trait HandleService {
  def handleGithub(id: String): Future[Either[ServiceResponse,
                                              UserGithubStat]]
  def handleVK(id: String): Future[Either[ServiceResponse, UserVKStat]]
  def handleFacebook(id: String): Future[Either[ServiceResponse,
                                                UserFacebookStat]]
  def getVkData(link: String): Future[(List[String], List[String])]
  def catchRepos(link: String): List[String]
  def fetchRepo(link: String): GithubFileInfo
}

class HandleServiceImpl(userService: UserService,
                        vkService: VkService,
                        githubService: GithubService) extends HandleService {

  def handleGithub(id: String): Future[Either[ServiceResponse,
                                              UserGithubStat]] = {
    if (ObjectId.isValid(id)) {
      val objectId = new ObjectId(id)
      userService.getByIdDirectly(objectId).flatMap {
        case Some(x) =>
          if (x.github_link != null) {
              val repos: List[String] = catchRepos(x.github_link.get).map { x =>
                x + "/git/trees/master?recursive=1"
              }
              println("repos= " + repos)
              val result: List[GithubFileInfo] = repos.map { repo =>
                fetchRepo(repo)
              }
            Future.successful(
              Left(ServiceResponse(false, Some("No github link!"))))
            //              getVkData(x.vk_link.get).flatMap { z =>
            //                vkService.create(id, z._1, z._2).map {
            //                  case Left(response) =>
            //                    Left(response)
            //                  case Right(stat) =>
            //                    Right(stat)
            //                }
            //              }
            //prepareData
          } else {
              Future.successful(
                Left(ServiceResponse(false, Some("No github link!"))))
          }
        case _ =>
          Future.successful(
            Left(ServiceResponse(false, Some("User not found!"))))
      }
    } else Future.successful(
      Left(ServiceResponse(false, Some("Request is not right"))))
  }

  def catchRepos(link: String): List[String] = {
    val nickname: Array[String] = link.split("/")
    val data=
      parse(Source.fromURL(s"https://api.github.com/users/${nickname.last}/repos").mkString)
    val extractedData: List[String] =
    for {
        JObject(x) <- data
        JField(field, JString(value)) <- x
        if field == "url"
    } yield value
    println("found="+extractedData)
    extractedData
  }

  def fetchRepo(link: String): GithubFileInfo = {
    val urls: List[String] =
      Http()
      .singleRequest(HttpRequest(
        HttpMethods.GET,
        Uri(link)
      ))
      .flatMap(Unmarshal(_).to[Option[GithubAPIMain]])
      .map(x => x.get.tree)
      .collect { x =>
        x if x.`type` == "blob" => x.url
      }

    println(urls)
    val kek = new GithubFileInfo("wrg", "rwrfwef")
    kek
  }

  def getVkData(link: String): Future[(List[String], List[String])] = {
    val data = VkRequestFormat.apply(link)
    Http()
      .singleRequest(HttpRequest(
        HttpMethods.POST,
        Uri(s"http://10.20.2.66:8080/"),
        entity = HttpEntity(ContentTypes.`application/json`, write(data))
      ))
      .flatMap(Unmarshal(_).to[Option[VkResponseFormat]])
      .map(x => println(x))
      Future.successful(List[("shit", "shit")])
//      .map { x =>
//        x match {
//          case Some(x) => (x.interests, x.rates)
//          case None => (List("sample"),List("sample"))
//        }
      //}
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
                  case Left(response) =>
                    Left(response)
                  case Right(stat) =>
                    Right(stat)
                }
              }
            case None =>
              Future.successful(
                Left(ServiceResponse(false, Some("No vk link!"))))
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
