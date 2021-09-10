package com.example.instagram

import cats.data.{Kleisli, OptionT}
import cats.effect.{Async, ConcurrentEffect, ContextShift, Timer}
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxEitherId, toSemigroupKOps}
import com.example.instagram.config.JwtConfig
import com.example.instagram.repos.impl.{MessageRepoInterpreter, UserRepoInterpreter}
import com.example.instagram.services.{AuthService, MessageService, UserService}
import doobie.util.transactor.Transactor
import io.circe.parser.decode
import org.http4s.Credentials.Token
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Authorization
import org.http4s.server.{AuthMiddleware, Router}
import org.http4s.server.middleware._
import org.http4s.syntax.all._
import org.http4s._
import pdi.jwt.{Jwt, JwtAlgorithm}

import scala.concurrent.duration.DurationInt

class Routes[F[_]: Async: ConcurrentEffect: Timer: ContextShift](val xa: Transactor[F], jwtConfig: JwtConfig) extends Http4sDsl[F] {
  private val authUser: Kleisli[F, Request[F], Either[String, User]] = Kleisli(req => {
    req.headers
      .get(Authorization)
      .collect {
        case Authorization(Token(AuthScheme.Bearer, token)) =>
          token
      }
      .fold("Bearer token not found".asLeft[User].pure[F]) { token =>
        Jwt
          .decode(token, jwtConfig.hmacSecret, JwtAlgorithm.allHmac())
          .toEither
          .fold(_ => "Invalid access token".asLeft, _.asRight)
          .map(c => decode[User](c.content).fold(
            _ =>
              "Token parsing error".asLeft,
            _.asRight))
          .flatten
          .pure[F]
      }
  })

  private val onFailure: AuthedRoutes[String, F] = Kleisli(req => OptionT.liftF(Forbidden(req.context)))

  private val authMiddleware: AuthMiddleware[F, User] = AuthMiddleware(authUser, onFailure)

  def make : HttpApp[F] = {

    val messageInterpreter: MessageRepoInterpreter[F] = MessageRepoInterpreter(xa)
    val messageService = MessageService(messageInterpreter)

    val userInterpreter: UserRepoInterpreter[F] = UserRepoInterpreter(xa)
    val userService = UserService(userInterpreter, jwtConfig)
    val authService = new AuthService(userService, jwtConfig)

    val userRoute: HttpRoutes[F]  = UserRoutes[F](userService).routes(authMiddleware)
    val routes = new MessageRoutes[F](messageService).routes <+> new LoginRoutes[F](authService).routes <+> userRoute

    val middleware: HttpRoutes[F] => HttpRoutes[F] = {
      { http: HttpRoutes[F] =>
        AutoSlash(http)
      } andThen { http: HttpRoutes[F] =>
        CORS(http, CORS.DefaultCORSConfig)
      } andThen { http: HttpRoutes[F] =>
        Timeout(60.seconds)(http)
      }
    }
    val loggers: HttpApp[F] => HttpApp[F] = {
      { http: HttpApp[F] =>
        RequestLogger.httpApp(logHeaders = true, logBody = true)(http)
      } andThen { http: HttpApp[F] =>
        ResponseLogger.httpApp(logHeaders = true, logBody = true)(http)
      }
    }

    loggers(middleware(Router[F]("api" -> routes)).orNotFound)
  }
}
