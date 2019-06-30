package com.hack.routes

import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.hack.services.GithubService
import com.hack.configs.Json4sSupport._

class GithubStatRoutes(githubService: GithubService) {
  def route = cors() {
    path("githubstats") {
      get {
        complete(githubService.getAll())
      } ~
        parameters("id".as[String]) {
          id =>
            post {
              complete(githubService.getSingleById(id))
            }
        } ~
        parameters("user".as[String]) {
          id =>
            post {
              complete(githubService.getSingleByUser(id))
            }
        }
    }
  }
}
