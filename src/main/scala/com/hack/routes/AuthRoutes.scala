package com.hack.routes

import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.hack.services.{AuthService, UserService}
import com.hack.configs.Json4sSupport._

class AuthRoutes(authService: AuthService) {
  def route = cors() {
    path("auth") {
      parameters("github_link".as[String]) {
        github_link =>
          post {
            complete(authService.auth(github_link))
          }
      }
    }
  }
}
