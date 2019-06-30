package com.hack.services

import akka.http.scaladsl.server.Directives._
import com.hack.daos._
import com.hack.routes._
import org.mongodb.scala.MongoDatabase

class HttpService(database: MongoDatabase) {

  //user
  val userDao =
    new UserDaoImpl(database.getCollection("users"))

  val userService =
    new UserServiceImpl(userDao)

  val userRoutes =
    new UserRoutes(userService)

  //vkStat
  val vkStatDao =
    new VkStatDaoImpl(database.getCollection("vkstats"))

  val vkService =
    new VkServiceImpl(
      vkStatDao,
      userService
    )

  val vkStatRoutes =
    new VkStatRoutes(vkService)

  //githubStat
  val githubStatDao =
    new GithubStatDaoImpl(database.getCollection("githubstats"))

  val githubService =
    new GithubServiceImpl(
      githubStatDao,
      userService
    )

  val githubStatRoutes =
    new GithubStatRoutes(githubService)

  //others
  val handleRoutes =
    new HandleRoutes(
      new HandleServiceImpl(
        userService,
        vkService,
        githubService
      )
    )

  val authRoutes =
    new AuthRoutes(
      new AuthServiceImpl(
        userDao
      )
    )

  val routes =
    pathPrefix("api") {
      handleRoutes.route ~ userRoutes.route ~ authRoutes.route ~
        githubStatRoutes.route ~ vkStatRoutes.route
    }

}
