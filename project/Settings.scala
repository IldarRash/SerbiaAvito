import Dependencies._
import sbt.Keys.{scalacOptions, _}
import sbt._

object Settings {

  val commonSettings =
    Seq(
      scalaVersion := "2.13.4",
      scalacOptions := Seq(
        "-deprecation",
        "-encoding",
        "utf-8",
        "-feature",
        "-unchecked",
        "-language:postfixOps",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-Xfatal-warnings"
      ),
      version := (version in ThisBuild).value,
      testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
      cancelable in Global := true,
      mainClass in Compile := Some("com.mts.bigdata.shrinker.Main"),
      javaOptions += "-Dlogback.configurationFile=/src/resources/logback.xml"
    )

  val serviceDependencies = List(cats, catsEffect, fs2Kafka)
  val routeDependencies = http4s
  val serverDependencies = List(logback, log4cats, pureConfig)
  val domainDependencies = circe
}