package com.example.cleverhouse

import cats.effect.Sync
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Request, Response}

class SensorsRoutes [F[_] : Sync] extends Http4sDsl[F] {
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "sensor" => hiResp(req)
  }

  def hiResp(req: Request[F]) : F[Response[F]] = Ok(s"Hello, ${req.params.get("name")}.")
}
