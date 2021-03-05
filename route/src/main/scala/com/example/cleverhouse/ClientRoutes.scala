package com.example.cleverhouse

import cats.effect.{ Sync}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class ClientRoutes [F[_] : Sync] extends Http4sDsl[F] {
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "client" / name  => Ok(s"Hello, $name.")
  }
}
