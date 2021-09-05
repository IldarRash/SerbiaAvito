package com.example.instagram

import cats.Monad
import cats.effect.Async
import cats.implicits._
import com.example.instagram.Message.{FromMessage, ToMessage}
import io.circe.{Decoder, Encoder}
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
      val mess = (from: FromMessage)  => refactor(from)
      handleReq(req)(mess)

    case GET -> Root / "message" =>
      Ok(ToMessage(EventId(UUID.randomUUID()), MessageId(UUID.randomUUID()), MessageBody("asdasd"), Timestamp(Instant.now()), false))
  }



  def handleReq[T1: Decoder, T2: Encoder](req: Request[F])(mess: T1 => T2): F[Response[F]] =
    for {
      msg <- req.asJsonDecode[T1]
        .map(mess)
      res  <-  Ok(msg)
    } yield res

  def refactor(msg: FromMessage) : ToMessage =
    ToMessage(msg.id, msg.messageId, msg.body, Timestamp(Instant.now()), false)

}
