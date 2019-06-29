package com.hack.routes

import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.hack.services.{HandleService, UserService}
import com.hack.configs.Json4sSupport._

class HandleRoutes(handleService: HandleService) {
  def route = cors() {
    path("handleGithub") {
      parameters("id".as[String]) {
        id =>
          post {
            complete(handleService.handleGithub(id))
          }
      }
    } ~
      path("handleVK") {
        parameters("id".as[String]) {
          id =>
            post {
              complete(handleService.handleVK(id))
            }
        }
      } ~
      path("handleFB") {
        parameters("id".as[String]) {
          id =>
            post {
              complete(handleService.handleFacebook(id))
            }
        }
      }
    }
  }
