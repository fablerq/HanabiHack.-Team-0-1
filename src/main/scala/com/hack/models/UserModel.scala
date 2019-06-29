package com.hack.models

import org.mongodb.scala.bson.ObjectId

case class User(
                  _id: ObjectId,
                  name: String,
                  github_link: Option[String] = None,
                  vk_link: Option[String],
                  facebook_link: Option[String]
                )

case class UserParams(
                   name: Option[String] = None,
                   github_link: Option[String] = None,
                   vk_link: Option[String] = None,
                   facebook_link: Option[String] = None
                      )
