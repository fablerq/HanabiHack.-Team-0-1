package com.hack.models

import org.mongodb.scala.bson.ObjectId

case class UserGithubStat(
                  _id: ObjectId,
                  user_id: String
                          )