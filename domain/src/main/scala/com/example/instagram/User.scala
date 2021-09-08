package com.example.instagram

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}


import scala.util.control.NoStackTrace

sealed case class Role(roleRepr: String)

case class User(
                 userId: Long,
                 firstName: String,
                 lastName: String,
                 password: String,
               )

case class UserRequest(
                        userId: Long,
                        firstName: String,
                        lastName: String,
                        password: String,
                      )

case class UserResponse(
                         userId: Long,
                         firstName: String,
                         lastName: Option[String],
                         token: String
                       )

case class UserCredential(username: String, password: String)

case class InvalidUserOrPassword(userName: String) extends NoStackTrace

case class UsernameAlreadyExist(userName: String) extends NoStackTrace

object Role {
  lazy val Customer: Role      = Role("User")
  lazy val Administrator: Role = Role("Administrator")

  implicit val encoder: Encoder[Role] = deriveEncoder
  implicit val decoder: Decoder[Role] = deriveDecoder
}

object UserResponse {
  def apply(user: User, token: String): UserResponse = new UserResponse(user.userId, user.firstName, Some(user.lastName), token)

  implicit val encoder: Encoder[UserResponse] = deriveEncoder
}

object UserRequest {
  implicit val decoder: Decoder[UserRequest] = deriveDecoder
}

object UserCredential {
  implicit val encoder: Encoder[UserCredential] = deriveEncoder
  implicit val decoder: Decoder[UserCredential] = deriveDecoder
}

object User {
  def toUserResponse(user: User) : UserResponse = new UserResponse(user.userId, user.firstName, Some(user.lastName), "")
  implicit val encoder: Encoder[User] = deriveEncoder
  implicit val decoder: Decoder[User] = deriveDecoder
}
