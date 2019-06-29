name := "HanabiHack"
version := "0.1"
scalaVersion := "2.12.8"

lazy val akkaHttpVersion = "10.1.7"
lazy val akkaVersion    = "2.5.21"

lazy val root = (project in file("."))
  .settings(

    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor"           % akkaVersion,
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

      "org.reactivestreams" % "reactive-streams" % "1.0.0",
      "ch.megard" %% "akka-http-cors" % "0.4.0",
      "org.json4s" %% "json4s-jackson" % "3.6.5",
      "net.liftweb" %% "lift-json" % "3.3.0",
      "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0",

      "org.scalatest"     %% "scalatest"            % "3.0.1"         % Test
    )
  )

enablePlugins(JavaAppPackaging)





