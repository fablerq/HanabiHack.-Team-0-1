package com.hack.models

import org.mongodb.scala.bson.ObjectId

case class UserGithubStat(
                  _id: ObjectId,
                  user_id: String
                          )

case class GithubFileInfo(
                 filename: String,
                 line: String
                         )

case class FrameworkResponse(
                 framework: String,
                 ratio: Long
                               )

case class GithubAPI(
                    path: String,
                    mode: Int,
                    `type`: String,
                    sha: String,
                    size: Int,
                    url: String
                    )

case class GithubAPIMain(
                        sha: String,
                        url: String,
                        tree: List[GithubAPI],
                        truncated: Boolean
                        )