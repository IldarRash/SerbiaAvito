package com.example.instagram

import cats.effect.{Async, ConcurrentEffect, ContextShift, Sync, Timer}
import cats.implicits.toSemigroupKOps
import com.example.instagram.config.JwtConfig
import com.example.instagram.repos.impl.{MessageRepoInterpreter, UserRepoInterpreter}
import com.example.instagram.services.{AuthService, MessageService, UserService}
import doobie.util.transactor.Transactor
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.middleware.{AutoSlash, CORS, RequestLogger, ResponseLogger, Timeout}

import scala.concurrent.duration.DurationInt

class Routes[F[_]: Async: ConcurrentEffect: Timer: ContextShift](val xa: Transactor[F], jwtConfig: JwtConfig) {
  def make : HttpApp[F] = {

    val messageInterpreter: MessageRepoInterpreter[F] = MessageRepoInterpreter(xa)
    val messageService = MessageService(messageInterpreter)

    val userInterpreter: UserRepoInterpreter[F] = UserRepoInterpreter(xa)
    val userService = UserService(userInterpreter)
    val authService = new AuthService(userService, jwtConfig)

    val routes = new MessageRoutes[F](messageService).routes <+> new LoginRoutes[F](authService).routes

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

    loggers(middleware(Router("api" -> routes)).orNotFound)
  }
}
