package com.example.instagram


import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import zio.interop.catz.implicits._
import zio.interop.catz.{taskConcurrentInstance, _}
import zio.{Task, ZEnv, ZIO, _}
import org.http4s.implicits._
import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._

object Main extends zio.App {
  type F[A] = Task[A]

  override def run(args: List[String]) = {

    val ec = platform.executor.asEC
    Resources
      .make[F]
      .use { case Resources(config, xa) =>

        val httpServer = Task.concurrentEffectWith { implicit CE =>
          val routes = new Routes[F](xa)
          BlazeServerBuilder[F](ec)
            .bindHttp(config.apiPort, config.apiHost)
            .withHttpApp(routes.make)
            .serve
            .compile
            .drain
        }

        Logger[F].info("Started server") *>
          httpServer
          .unit
      }
      .exitCode
  }
}