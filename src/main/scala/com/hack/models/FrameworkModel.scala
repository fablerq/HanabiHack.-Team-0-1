package com.hack.models

import org.mongodb.scala.bson.ObjectId

case class Framework(
                       _id: ObjectId,
                       title: String,
                       image_url: String,
                     )