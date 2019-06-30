package com.hack.routes

import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.hack.services.{UserService, VkService}
import com.hack.configs.Json4sSupport._

class VkStatRoutes(vkService: VkService) {
  def route = cors() {
    path("vkstats") {
      get {
        complete(vkService.getAll())
      } ~
        parameters("id".as[String]) {
          id =>
            post {
              complete(vkService.getSingleById(id))
            }
        } ~
        parameters("user".as[String]) {
          id =>
            post {
              complete(vkService.getSingleByUser(id))
            }
        }
    }
  }
}
