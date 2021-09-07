package com.example.instagram.repos

import com.example.instagram.{User, UserRequest}

trait UserRepo[F[_]] {
  def addUser(userRequest: UserRequest): F[Int]

  def updateUser(user: User): F[Int]

  def getUserById(userId: Long): F[Option[User]]

  def deleteUserById(userId: Long): F[Int]

  def getUserByUserName(username: String): F[Option[User]]

}