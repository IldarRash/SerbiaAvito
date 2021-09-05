import Dependencies._
import com.typesafe.sbt.packager.Keys._
import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtOnCompile
import sbt.Keys.{scalacOptions, _}
import sbt._

object Settings {

  val commonSettings =
    Seq(
      scalaVersion := "2.13.6",
      scalacOptions := Seq(
        "-Ymacro-annotations",
        "-deprecation",
        "-encoding", "utf-8",
        "-explaintypes",
        "-feature",
        "-unchecked",
        "-language:postfixOps",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-Xcheckinit",
        "-Xfatal-warnings"
      ),
      version := (version in ThisBuild).value,
      scalafmtOnCompile := true,
      testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
      javaOptions += "-Dlogback.configurationFile=/src/resources/logback.xml",
      mainClass in Compile := Some("com.example.cleverhouse.Main"),

      addCompilerPlugin(contextApplied),
      addCompilerPlugin(kindProjector),

      dockerBaseImage := "openjdk:jre-alpine",
      dockerUpdateLatest := true
    )

  val serviceDependencies = List(cats, catsEffect, neutronCore, slf4j, zioCats) ++ zioTest
  val routeDependencies = http4s
  val serverDependencies = List(neutronCirce, ciris, pureConfig, logback, log4cats) ++ zio
  val domainDependencies = List(newtype) ++ circe
}