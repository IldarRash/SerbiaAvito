package com.example

import java.time.Instant
import java.util.UUID

import cats.Eq
import cats.implicits._
import io.circe.{ Decoder, Encoder }
import io.estatico.newtype.Coercible
import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops._

package object instagram {

  @newtype case class Timestamp(value: Instant)

  @newtype case class EventId(value: String)
  object EventId {
    def apply(uuid: UUID): EventId = EventId(uuid.toString)
  }

  @newtype case class MessageBody(value: String)

  @newtype case class MessageId(value: String)
  object MessageId {
    def apply(uuid: UUID): MessageId = MessageId(uuid.toString)
  }

  sealed trait DbError
  object DbError {
    case class UserNameDbError(userName: String) extends DbError
  }

  implicit def coercibleEq[A: Coercible[B, *], B: Eq]: Eq[A] =
    Eq[B].contramap[A](_.asInstanceOf[B])

  implicit def coercibleDecoder[A: Coercible[B, *], B: Decoder]: Decoder[A] =
    Decoder[B].map(_.coerce[A])

  implicit def coercibleEncoder[A: Coercible[B, *], B: Encoder]: Encoder[A] =
    Encoder[B].contramap(_.asInstanceOf[B])
}
