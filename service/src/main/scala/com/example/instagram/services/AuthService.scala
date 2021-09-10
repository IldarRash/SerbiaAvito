package com.example.instagram.services

import cats.Monad
import cats.effect.Async
import cats.implicits._
import cats.implicits.catsSyntaxApplicativeErrorId
import com.example.instagram.config.JwtConfig
import com.example.instagram.{Crypto, InvalidUserOrPassword, Logger, User, UserResponse}
import io.circe.syntax.EncoderOps
import pdi.jwt.{Jwt, JwtAlgorithm}

class AuthService[F[_]: Monad : Async: Logger](userService: UserService[F], jwtConfig: JwtConfig) extends AuthHelper(jwtConfig) {

  def login(email: String, password: String): F[UserResponse] =
    for {
      user <- userService
        .getUserByEmail(email)
      checkedUser <- user.filter(user => Crypto.checkPassword(password, user.password)).pure[F]
      token       <- checkedUser.fold(InvalidUserOrPassword(email).raiseError[F, UserResponse])(u => encode(u).pure[F])
    } yield token

  //TODO: Implement session/timeout for jwt-token
  def logout(token: String, username: String): F[Unit] = ???
}
