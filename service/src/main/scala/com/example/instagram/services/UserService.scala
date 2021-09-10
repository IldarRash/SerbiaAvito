package com.example.instagram.services

import cats.Monad
import cats.effect.Async
import com.example.instagram.{DbError, Logger, User, UserRequest, UserResponse}
import cats.implicits._
import com.example.instagram.config.JwtConfig
import com.example.instagram.repos.UserRepo

import java.util.UUID

class UserService [F[_] : Monad : Async : Logger](userRepo: UserRepo[F], jwtConfig: JwtConfig) extends AuthHelper(jwtConfig) {
  def addUser(userRequest: UserRequest): F[UserResponse] =
     userRepo.addUser(userRequest)
       .map(encode)


  def validated(req: Int, username: String): Either[DbError, String] = {
    if (req > 0)
      Right(username)
    else {
      val e: DbError = DbError.UserNameDbError(username)
      Left(e)
    }
  }

  def getAllUsers(user: User): fs2.Stream[F, User] = {
    println(user)
    userRepo.all
  }

  def updateUser(user: User): F[User] =
    userRepo.updateUser(user)

  def getUserById(userId: Long): F[Option[User]] =
    userRepo.getUserById(userId)

  def deleteUserById(userId: Long): F[Int]=
    userRepo.deleteUserById(userId)

  def getUserByEmail(email: String): F[Option[User]]=
    userRepo.getUserByEmail(email)
}
object UserService {
  def apply[F[_]: Async : Logger](userRepo: UserRepo[F], jwtConfig: JwtConfig): UserService[F] =
    new UserService[F](userRepo, jwtConfig)
}
