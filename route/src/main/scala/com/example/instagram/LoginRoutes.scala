package com.example.instagram

import cats.effect.Async
import cats.syntax.all._
import com.example.instagram.services.AuthService
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf, toMessageSynax}
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes, Request}

class LoginRoutes[F[_]: Async : Logger](
                                       authService: AuthService[F]
                                     ) extends Http4sDsl[F] {


  implicit def jsonDecoder[A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
  implicit def jsonEncoder[A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case req @ POST -> Root / "login" =>

      handleReq(req)(user => authService.login(user.email, user.password))
        .flatMap(Ok(_))
        .handleErrorWith {
      case InvalidUserOrPassword(_) => Forbidden()
      }
  }

    def handleReq(req: Request[F])(mess: UserCredential => F[UserResponse]): F[UserResponse] =
      for {
        msg <- req.asJsonDecode[UserCredential]
        jwt    <- mess(msg)
      } yield jwt
}
