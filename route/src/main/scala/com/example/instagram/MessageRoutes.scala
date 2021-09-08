package com.example.instagram

import cats.Monad
import cats.effect.Async
import cats.implicits._
import com.example.instagram.Message.InstagramMessage
import com.example.instagram.services.MessageService
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.syntax.EncoderOps
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.{JsonDecoder, toMessageSynax}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Request, Response}

import java.time.Instant

class MessageRoutes [F[_]: Monad : Async : JsonDecoder](messageService: MessageService[F]) extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "message" =>
      val mess = (from: InstagramMessage)  => refactor(from)
      handleReq(req)(mess)

    case GET -> Root / "message" =>
      for {
        json <- Ok(messageService.all.map(_.asJson))
      } yield json
  }

  def handleReq(req: Request[F])(mess: InstagramMessage => InstagramMessage): F[Response[F]] =
    for {
      msg <- req.asJsonDecode[InstagramMessage]
      answer    <- messageService.addMessage(msg)
      res  <-  Ok(answer)
    } yield res

  def refactor(msg: InstagramMessage) : InstagramMessage =
    InstagramMessage(msg.id, msg.eventId, msg.body, true, Instant.now())

}


object MessageRoutes {
  def apply[F[_]: Async](messageService: MessageService[F]): MessageRoutes[F] =
    new MessageRoutes[F](messageService)
}