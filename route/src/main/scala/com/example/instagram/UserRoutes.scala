package com.example.instagram

import cats.effect._
import cats.syntax.all._
import com.example.instagram.services.UserService
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import org.http4s.{AuthedRoutes, HttpRoutes}

class UserRoutes[F[_]: Sync: Logger](
                                      userService: UserService[F]
                                    ) extends Http4sDsl[F] {

  private val prefixPath = "/users"

  private val userCreateRoutes = HttpRoutes.of[F] {
    case req @ POST -> Root =>
      (for {
        userRequest <- req.decodeJson[UserRequest]
        hash        = Crypto.encrypt(userRequest.password)
        userRes <- userService
          .addUser(userRequest.copy(password = hash))
        result <- Ok(userRes)
      } yield result).recoverWith {
        case UsernameAlreadyExist(username) => Conflict(s"Username already exist: $username")
      }
  }

  private val httpRoutes: AuthedRoutes[User, F] = AuthedRoutes.of {
    case GET -> Root / LongVar(userId) as _ =>
      userService
        .getUserById(userId)
        .flatMap {
          case Some(user) => Ok(User.toUserResponse(user).asJson)
          case None       => NotFound(s"User with user id $userId not found".asJson)
        }

    case GET -> Root / "all"  as user =>
      for {
        answer <- Ok(userService
           .getAllUsers(user)
           .map(User.toUserResponse)
           .map(_.asJson)
        )
       } yield answer
   /* case DELETE -> Root / LongVar(userId) as _ =>
      userService
        .deleteUserById(userId)
        .map(Option.apply)
        .flatMap {
          case _ => Ok(userId)
          case Some(0) => NotFound(s"User with user id $userId not found".asJson)
        }*/
  }

  def routes(authMiddleware: AuthMiddleware[F, User]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes),
    prefixPath -> userCreateRoutes
  )

}

object UserRoutes {
  def apply[F[_]: Sync: Logger](
                                 userService: UserService[F]
                               )(
                                 implicit F: ConcurrentEffect[F]
                               ): UserRoutes[F] =
    new UserRoutes(
      userService
    )
}
