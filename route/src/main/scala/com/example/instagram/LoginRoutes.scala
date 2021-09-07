package com.example.instagram

import cats.effect.{Async, Sync}
import dev.profunktor.auth.jwt.JwtToken
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf, toMessageSyntax}
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes, Request, Response}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import cats.effect.Sync
import cats.syntax.all._
import com.example.instagram.Message.InstagramMessage
import com.example.instagram.services.AuthService

class LoginRoutes[F[_]: Async : Logger](
                                       authService: AuthService[F]
                                     ) extends Http4sDsl[F] {

  implicit val tokenEncoder: Encoder[JwtToken] =
    Encoder.forProduct1("access_token")(_.value)

  implicit def jsonDecoder[A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
  implicit def jsonEncoder[A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]

  val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case req @ POST -> Root / "login" =>

      handleReq(req)(user => authService.login(user.userName, user.password))
        .flatMap(Ok(_))
        .handleErrorWith {
      case InvalidUserOrPassword(_) => Forbidden()
      }
  }

    def handleReq(req: Request[F])(mess: UserCredential => F[JwtToken]): F[JwtToken] =
      for {
        msg <- req.asJsonDecode[UserCredential]
        jwt    <- mess(msg)
      } yield jwt
}
