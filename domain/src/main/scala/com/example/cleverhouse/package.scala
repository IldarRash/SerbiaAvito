package com.example

import io.circe._, io.circe.generic.semiauto._
import java.time.Instant

package object data {
  case class EventId(value: Long) extends AnyVal
  object EventId {
    def apply(value: Long) : EventId = EventId(value)
  }

  case class Timestamp(value: Instant)
  object Timestamp {
    def apply(value: Instant) : Timestamp = Timestamp(value)
  }

  sealed trait SensorEvent {
    def id: EventId
    def created: Timestamp
  }

  object SensorEvent {
    final case class SensorLight(id: EventId, created: Timestamp) extends SensorEvent


    implicit val eventEncoder: Encoder[SensorEvent] = deriveEncoder
    implicit val eventDecoder: Decoder[SensorEvent] = deriveDecoder
  }
}
