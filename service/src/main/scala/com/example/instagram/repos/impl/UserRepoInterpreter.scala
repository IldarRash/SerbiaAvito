package com.example.instagram.repos.impl

import cats.effect.Async
import com.example.instagram.{User, UserRequest}
import com.example.instagram.repos.{Mapping, UserRepo}
import doobie.implicits.toSqlInterpolator
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.postgres._
import doobie.postgres.implicits._

class UserRepoInterpreter [F[_]: Async](xa: Transactor[F]) extends UserRepo[F] with Mapping{
  override def addUser(userRequest: UserRequest): F[Int] =
    UserRepoInterpreter.addUser(userRequest)
      .run
      .transact(xa)

  override def updateUser(user: User): F[Int] =
    UserRepoInterpreter.updateUser(user)
      .run
      .transact(xa)

  override def getUserById(userId: Long): F[Option[User]] =
    UserRepoInterpreter.getUserById(userId)
      .option
      .transact(xa)

  override def deleteUserById(userId: Long): F[Int] =
    UserRepoInterpreter.deleteUserById(userId)
      .run
      .transact(xa)

  override def getUserByUserName(username: String): F[Option[User]] =
    UserRepoInterpreter.getUserByUserName(username)
      .option
      .transact(xa)
}

object UserRepoInterpreter {
  def apply[F[_]: Async](xa: Transactor[F]
                        ): UserRepoInterpreter[F] =
    new UserRepoInterpreter[F](xa)


  def addUser(userRequest: UserRequest): doobie.Update0 =
    sql"""
         |INSERT INTO app_user (
         |  first_name,
         |  last_name,
         |  password
         |)
         |VALUES (
         |  ${userRequest.firstName},
         |  ${userRequest.lastName},
         |  ${userRequest.password}
         |)
     """.stripMargin
      .update

  def updateUser(user: User): doobie.Update0 =
    sql"""
         |UPDATE app_user (
         |  first_name,
         |  last_name,
         |  password
         |)
         |VALUES (
         |  ${user.firstName},
         |  ${user.lastName},
         |  ${user.password}
         |)
       """.stripMargin
      .update

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

  def getUserByUserName(username: String) : doobie.Query0[User] =
    sql"""
         |SELECT * FROM app_user usr
         |WHERE usr.first_name = ${username}
       """.stripMargin
      .query[User]
}
