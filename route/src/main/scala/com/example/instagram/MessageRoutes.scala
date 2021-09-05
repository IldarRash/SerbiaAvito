package com.example.instagram

import cats.Monad
import cats.effect.Async
import cats.implicits._
import com.example.instagram.Message.ToMessage
import io.circe.Decoder
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.{JsonDecoder, toMessageSynax}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Request, Response}

import java.time.Instant
import java.util.UUID

class MessageRoutes [F[_]: Monad : Async : JsonDecoder] extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "message" =>
      val sensor = (s: Message)  => hiSendor(s)
      handleReq(req)(sensor)

    case GET -> Root / "message" =>
      Ok(ToMessage(EventId(UUID.randomUUID()), MessageId(UUID.randomUUID()), MessageBody("asdasd"), Timestamp(Instant.now())))
  }



  def handleReq[T: Decoder](req: Request[F])(mess: T => F[String]): F[Response[F]] =
    for {
      msg <- req.asJsonDecode[T]
      ans  <- mess(msg)
      res  <-  Ok(ans)
    } yield res

  def hiSendor(s: Message) : F[String] = Monad[F].pure("Hi" + s.id)
}
