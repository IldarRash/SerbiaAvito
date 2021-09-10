package com.example.instagram.repos

import com.example.instagram.{User, UserRequest}

trait UserRepo[F[_]] {
  def addUser(userRequest: UserRequest): F[User]

  def updateUser(user: User): F[User]

  def all: fs2.Stream[F, User]

  def getUserById(userId: Long): F[Option[User]]

  def deleteUserById(userId: Long): F[Int]

  def getUserByEmail(email: String): F[Option[User]]

}
