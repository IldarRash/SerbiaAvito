package com.example.cleverhouse

import cats.Monad
import cats.effect.Async
import cats.data._
import cats.implicits._
import com.example.data.SensorEvent
import io.circe.Decoder
import cats.syntax.flatMap
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.{JsonDecoder, toMessageSynax}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Request, Response}

class SensorsRoutes [F[_]: Monad : Async : JsonDecoder] extends Http4sDsl[F] {
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "sensor" =>
      val sensor = (s: SensorEvent)  => hiSendor(s)
      handleReq(req)(sensor)
  }

  def handleReq[T: Decoder](req: Request[F])(sens: T => F[String]): F[Response[F]] =
    for {
      sensor <- req.asJsonDecode[T]
      ans  <- sens(sensor)
      res  <-  Ok(ans)
    } yield res

  def hiSendor(s: SensorEvent) : F[String] = Monad[F].pure("Hi" + s.id)
}
