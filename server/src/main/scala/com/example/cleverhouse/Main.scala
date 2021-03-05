package com.example.cleverhouse

import cats.effect.{ContextShift, ExitCode, IO, IOApp, Timer}
import com.example.cleverhouse.config.AppConfig
import org.http4s.HttpApp
import pureconfig.ConfigSource
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.global

object Main extends IOApp {
  type Init[T] = IO[T]
  type Routes[F[_]] = HttpApp[F]


  override def run(args: List[String]): IO[ExitCode] = {
    implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
    implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

    startApp()
      .as(ExitCode.Success)
  }


  def startApp(): Init[Unit] = {
    val appConfig = ConfigSource.default.loadOrThrow[AppConfig]
    HttpServer.start[Init](appConfig, Routes.make[Init]())
  }
}
