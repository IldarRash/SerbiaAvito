import sbt._
import Settings._

lazy val domain = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= domainDependencies)

lazy val service = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= serviceDependencies)
  .dependsOn(domain)

lazy val route = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= routeDependencies)
  .dependsOn(service)

lazy val server = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= serverDependencies)
  .dependsOn(route)

lazy val `instagram-bot` = Project("instagram-bot", file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings)
  .settings(organization := "com.example.instagram")
  .settings(moduleName := "instagram-bot")
  .settings(name := "instagram-bot")
  .aggregate(
    domain,
    service,
    route,
    server
  )
  .dependsOn(
    domain,
    service,
    route,
    server
  )