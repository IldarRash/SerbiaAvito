package com.example.instagram.services

import cats.Monad
import cats.effect.Async
import cats.implicits._
import cats.implicits.catsSyntaxApplicativeErrorId
import com.example.instagram.config.JwtConfig
import com.example.instagram.{Crypto, InvalidUserOrPassword, Logger, User}
import io.circe.syntax.EncoderOps
import pdi.jwt.{Jwt, JwtAlgorithm}

class AuthService[F[_]: Monad : Async: Logger](userService: UserService[F], jwtConfig: JwtConfig) {

  def login(username: String, password: String): F[String] =
    for {
      user <- userService
        .getUserByUserName(username)
      checkedUser <- user.filter(user => Crypto.checkPassword(password, user.password)).pure[F]
      token       <- checkedUser.fold(InvalidUserOrPassword(username).raiseError[F, String])(u => encode(u).pure[F])
    } yield token

  private def encode(user: User): String = Jwt.encode(user.asJson.toString(), jwtConfig.hmacSecret, JwtAlgorithm.HS256)

  //TODO: Implement session/timeout for jwt-token
  def logout(token: String, username: String): F[Unit] = ???
}