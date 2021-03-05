package com.example.cleverhouse

import cats.effect.Async
import com.example.data.SensorEvent
import io.circe.Decoder
import org.http4s.circe.{JsonDecoder, toMessageSynax}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Request, Response}

class SensorsRoutes [F[_] : Async : JsonDecoder] extends Http4sDsl[F] {
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "sensor" =>
      val sensor = (s: SensorEvent)  => hiSendor(s)
      handleReq(req)(sensor)
  }

  def handleReq[T: Decoder](req: Request[F])(sens: T => Some[String]): F[Response[F]] =
    for {
      // For simplicity UserId is randomly generated.
      // Normally, it would be taken from request
      sensor <- req.asJsonDecode[T]
      ans  <- sens(sensor)
      res  <-  Ok(ans)
    } yield res

  def hiSendor(s: SensorEvent) : Some[String] = Some("Hello, " + s.id)
}
