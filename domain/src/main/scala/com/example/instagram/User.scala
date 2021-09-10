package com.example.instagram

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}


import scala.util.control.NoStackTrace

sealed case class Role(roleRepr: String)

case class User(
                 userId: Long,
                 username: String,
                 email: String,
                 password: String,
                 bio: Option[String],
                 image: Option[String],
               )

case class UserRequest(
                        username: String,
                        email: String,
                        password: String,
                      )

case class UserResponse(
                         username: String,
                         email: String,
                         bio: Option[String],
                         image: Option[String],
                         token: String
                       )

case class UserCredential(email: String, password: String)

case class InvalidUserOrPassword(userName: String) extends NoStackTrace

case class UsernameAlreadyExist(userName: String) extends NoStackTrace

object Role {
  lazy val Customer: Role      = Role("User")
  lazy val Administrator: Role = Role("Administrator")

  implicit val encoder: Encoder[Role] = deriveEncoder
  implicit val decoder: Decoder[Role] = deriveDecoder
}

object UserResponse {
  def apply(user: User, token: String): UserResponse = new UserResponse(user.username, user.email, user.bio, user.image, token)

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
  def toUserResponse(user: User) : UserResponse =  UserResponse(user.username, user.email, user.bio, user.image, "token")
  implicit val encoder: Encoder[User] = deriveEncoder
  implicit val decoder: Decoder[User] = deriveDecoder
}
