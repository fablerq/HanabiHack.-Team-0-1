package com.hack.routes

import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.hack.services.UserService
import com.hack.configs.Json4sSupport._

class UserRoutes(userService: UserService) {
  def route = cors() {
    path("user") {
      get {
        complete(userService.getAll())
      } ~
        parameters("id".as[String]) {
          id =>
            post {
              complete(userService.getSingleById(id))
            }
        } ~
        parameters("link".as[String]) {
          id =>
            post {
              complete(userService.getSingleByLink(id))
            }
        } ~
        parameters("createdLink".as[String]) {
          id =>
            post {
              complete(userService.create(id))
            }
        }
    } ~
    path("updateInfo") {
      parameters("id".as[String], "github_link".as[String]) {
        (id, github_link) =>
          post {
            complete(userService.updateGithub(id, github_link))
          }
      } ~
        parameters("id".as[String], "vk_link".as[String]) {
          (id, vk_link) =>
            post {
              complete(userService.updateVK(id, vk_link))
            }
        } ~
        parameters("id".as[String], "facebook_link".as[String]) {
          (id, facebook_link) =>
            post {
              complete(userService.updateFacebook(id, facebook_link))
            }
        }
    }
  }
}
