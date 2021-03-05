import sbt._

object Dependencies {
  val cats = "org.typelevel" %% "cats-core" % Version.cats
  val catsEffect = "org.typelevel" %% "cats-effect" % Version.cats

  val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % Version.fs2Kafka

  val circe = List(
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-parser"
  ).map(_ % Version.circe)

  val http4s = List(
    "org.http4s" %% "http4s-dsl",
    "org.http4s" %% "http4s-circe",
    "org.http4s" %% "http4s-blaze-server"
  ).map(_ % Version.http4s)

  val pureConfig = "com.github.pureconfig" %% "pureconfig" % Version.pureConfigVersion

  val logback = "ch.qos.logback" % "logback-classic" % Version.logback
  val log4cats = "io.chrisdavenport" %% "log4cats-core" % Version.log4cats
}

object Version {
  val pureConfigVersion = "0.12.3"
  val fs2Kafka = "1.3.1"
  val cats = "2.3.1"
  val http4s = "1.0-234-d1a2b53"
  val circe = "0.13.0"
  val logback = "1.2.3"
  val log4cats = "1.1.1"
}