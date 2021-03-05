package com.example.cleverhouse

import cats.effect.Async
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class ClientRoutes [F[_] : Async] extends Http4sDsl[F] {
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "client" / name  => Ok(s"Hello, $name.")
  }
}
