import sbt._

object Dependencies {
  val zioCore = "dev.zio" %% "zio" % Version.zio
  val zioCats = ("dev.zio" %% "zio-interop-cats" % Version.zioCats).excludeAll(ExclusionRule("dev.zio"))
  val zio = List(zioCore, zioCats)

  val cats = "org.typelevel" %% "cats-core" % Version.cats
  val catsEffect = "org.typelevel" %% "cats-effect" % Version.catsEffect

  val zioTestCore = "dev.zio" %% "zio-test"    % Version.zio % Test
  val zioTestSbt = "dev.zio" %% "zio-test-sbt" % Version.zio % Test
  val zioTest = List(zioTestCore, zioTestSbt)

  val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % Version.http4s,
    "org.http4s" %% "http4s-circe" % Version.http4s,
    "org.http4s" %% "http4s-blaze-server" % Version.http4s
  )

  val fs2Core = "co.fs2" %% "fs2-core" % Version.fs2Core

  val newtype = "io.estatico" %% "newtype" % Version.newtype

  val circeGeneric = "io.circe" %% "circe-generic" % Version.circe
  val circeCore = "io.circe" %% "circe-core" % Version.circe
  val circeParser = "io.circe" %% "circe-parser" % Version.circe
  val circe = List(circeGeneric, circeCore, circeParser)

  val slf4j = "org.slf4j" % "slf4j-simple" % Version.slf4j

  val jwt = "dev.profunktor" %% "http4s-jwt-auth" % Version.jwtVersion
  val bcrypt = "org.mindrot" % "jbcrypt" % Version.bcrypt

  val doobieCore = "org.tpolecat" %% "doobie-core" % Version.doobie
  val doobiePostgres = "org.tpolecat" %% "doobie-postgres" % Version.doobie
  val doobieHikary = "org.tpolecat" %% "doobie-hikari" % Version.doobie
  val flyway = "org.flywaydb" % "flyway-core" % Version.flyway

  val ciris = "is.cir" %% "ciris" % Version.ciris

  val contextApplied = "org.augustjune" %% "context-applied" % Version.contextApplied
  val kindProjector = "org.typelevel" %% "kind-projector" % Version.kindProjector cross CrossVersion.full

  val pureConfig = "com.github.pureconfig" %% "pureconfig" % Version.pureConfigVersion
}

object Version {
  val zio = "1.0.11"
  val cats = "2.6.1"
  val catsEffect = "3.1.1"
  val zioCats = "3.1.1.0"
  val jwtVersion = "1.0.0"
  val bcrypt = "0.4"
  val fs2Core = "3.0.6"
  val slf4j = "1.7.32"
  val http4s = "0.23.3"
  val kindProjector = "0.13.2"
  val ciris = "2.1.1"
  val circe = "0.14.1"
  val newtype = "0.4.4"
  val neutron = "0.0.4"
  val doobie = "1.0.0-RC1"
  val postgres = "42.1.4"
  val contextApplied = "0.1.4"
  val pureConfigVersion = "0.12.3"
  val flyway = "6.3.2"
}