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
                        firstName: String,
                        lastName: String,
                        password: String,
                      )

case class UserResponse(
                         userId: Long,
                         firstName: String,
                         lastName: Option[String]
                       )

case class UserCredential(userName: String, password: String)

case class InvalidUserOrPassword(userName: String) extends NoStackTrace

case class UsernameAlreadyExist(userName: String) extends NoStackTrace

object Role {
  lazy val Customer: Role      = Role("User")
  lazy val Administrator: Role = Role("Administrator")

  implicit val encoder: Encoder[Role] = deriveEncoder
  implicit val decoder: Decoder[Role] = deriveDecoder
}

object UserResponse {
  implicit def userRequestEnc[F[_]: Sync]: EntityEncoder[F, UserResponse] = jsonEncoderOf
  implicit def userRequestDec[F[_]: Sync]: EntityDecoder[F, UserResponse] = jsonOf
}

object UserRequest {
  implicit def userRequestEnc[F[_]: Sync]: EntityEncoder[F, UserRequest] = jsonEncoderOf
  implicit def userRequestDec[F[_]: Sync]: EntityDecoder[F, UserRequest] = jsonOf
}

object UserCredential {
  implicit val encoder: Encoder[UserCredential] = deriveEncoder
  implicit val decoder: Decoder[UserCredential] = deriveDecoder
}

object User {
  implicit val encoder: Encoder[User] = deriveEncoder
  implicit val decoder: Decoder[User] = deriveDecoder
}