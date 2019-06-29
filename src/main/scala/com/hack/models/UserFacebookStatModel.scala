package com.hack.models

import org.mongodb.scala.bson.ObjectId

case class UserFacebookStat(
                       _id: ObjectId,
                       user_id: String,
                       interests: List[String],
                       rates: List[String]
                     )
