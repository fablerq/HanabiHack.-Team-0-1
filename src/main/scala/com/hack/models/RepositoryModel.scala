package com.hack.models

import org.mongodb.scala.bson.ObjectId

case class Repository(
                       _id: ObjectId,
                       githubStat_id: String,
                       url: String,
                       text: Option[String] = None,
                       //framework_id: Option[List[String]] = None,
                       rate: Option[String] = None
                     )
