package com.hack.models

import org.mongodb.scala.bson.ObjectId

case class UserVKStat(
                 _id: ObjectId,
                 user_id: String,
                 interests: List[String],
                 rates: List[String]
               )

case class VkResponseFormat(
                interests: List[String],
                rates: List[String]
                           )

case class VkRequestFormat(
                vk: String
                )