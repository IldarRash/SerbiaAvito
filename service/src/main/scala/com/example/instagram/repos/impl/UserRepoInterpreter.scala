package com.example.instagram.repos.impl

import cats.Comonad
import cats.effect.Async
import com.example.instagram.repos.{Mapping, UserRepo}
import com.example.instagram.{User, UserRequest}
import doobie.implicits._
import doobie.util.transactor.Transactor

class UserRepoInterpreter [F[_]: Async](xa: Transactor[F]) extends UserRepo[F] with Mapping{
  override def addUser(userRequest: UserRequest): F[User] =
    UserRepoInterpreter.addUser(userRequest)
      .transact(xa)

  override def updateUser(user: User): F[User] =
    UserRepoInterpreter.updateUser(user)
      .transact(xa)

  override def getUserById(userId: Long): F[Option[User]] =
    UserRepoInterpreter.getUserById(userId)
      .option
      .transact(xa)

  override def deleteUserById(userId: Long): F[Int] =
    UserRepoInterpreter.deleteUserById(userId)
      .run
      .transact(xa)

  override def getUserByEmail(email: String): F[Option[User]] =
    UserRepoInterpreter.getUserByEmail(email)
      .option
      .transact(xa)

  override def all: fs2.Stream[F, User] =
    UserRepoInterpreter
      .all
      .stream
      .transact(xa)

}

object UserRepoInterpreter {
  def apply[F[_]: Async](xa: Transactor[F]
                        ): UserRepoInterpreter[F] =
    new UserRepoInterpreter[F](xa)


  def addUser(userRequest: UserRequest): doobie.ConnectionIO[User] =
    sql"""
         |INSERT INTO app_user (
         |  username,
         |  email,
         |  pass
         |)
         |VALUES (
         |  ${userRequest.username},
         |  ${userRequest.email},
         |  ${userRequest.password}
         |)
     """.stripMargin
      .update
      .withUniqueGeneratedKeys[User](
        "app_user_id",
        "username",
        "email",
        "pass",
        "bio",
        "image"
      )

  def updateUser(user: User): doobie.ConnectionIO[User] =
    sql"""
         |UPDATE app_user (
         |  username,
         |  email,
         |  pass,
         |  bio,
         |  image
         |)
         |VALUES (
         |  ${user.username},
         |  ${user.email},
         |  ${user.password},
         |  ${user.bio},
         |  ${user.image}
         |)
       """.stripMargin
      .update
      .withUniqueGeneratedKeys[User](
        "id",
        "username",
        "email",
        "pass",
        "bio",
        "image")

  def getUserById(userId: Long): doobie.Query0[User] =
    sql"""
         |SELECT * FROM app_user usr
         |WHERE usr.app_user_id = ${userId}
       """.stripMargin
      .query[User]

  def deleteUserById(userId: Long): doobie.Update0 =
    sql"""
         |DELETE app_user where app_user.app_user_id = ${userId}
       """.stripMargin
      .update

  def getUserByEmail(email: String) : doobie.Query0[User] =
    sql"""
         |SELECT * FROM app_user usr
         |WHERE usr.email = ${email}
       """.stripMargin
      .query[User]

  def all: doobie.Query0[User] =
    sql"""SELECT * FROM app_user"""
      .stripMargin
      .query[User]
}
