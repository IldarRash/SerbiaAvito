package com.example.instagram.services

import cats.Monad
import cats.effect.Async
import com.example.instagram.{DbError, Logger, User, UserRequest}
import cats.implicits._
import com.example.instagram.repos.UserRepo

import java.util.UUID

class UserService [F[_] : Monad : Async : Logger](userRepo: UserRepo[F]) {
  def addUser(userRequest: UserRequest): F[Int] =
     userRepo.addUser(userRequest)


  def validated(req: Int, username: String): Either[DbError, String] = {
    if (req > 0)
      Right(username)
    else {
      val e: DbError = DbError.UserNameDbError(username)
      Left(e)
    }
  }

  def updateUser(user: User): F[Int] =
    userRepo.updateUser(user)

  def getUserById(userId: Long): F[Option[User]] =
    userRepo.getUserById(userId)

  def deleteUserById(userId: Long): F[Int]=
    userRepo.deleteUserById(userId)

  def getUserByUserName(username: String): F[Option[User]]=
    userRepo.getUserByUserName(username)
}
object UserService {
  def apply[F[_]: Async : Logger](userRepo: UserRepo[F]): UserService[F] =
    new UserService[F](userRepo)
}