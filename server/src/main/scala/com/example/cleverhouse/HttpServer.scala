package com.example.cleverhouse

import cats.effect.{ConcurrentEffect, Sync, Timer}
import com.example.cleverhouse.Main.Routes
import com.example.cleverhouse.config.AppConfig
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object HttpServer {
  def start[F[_] : ConcurrentEffect : Timer](
                        config: AppConfig,
                        routes: Routes[F]): F[Unit] =
    BlazeServerBuilder[F](ExecutionContext.global)
      .bindHttp(config.apiPort, config.apiHost)
      .withHttpApp(routes)
      .serve
      .compile
      .drain
}
