package com.example.instagram

import com.example.instagram.{EventId, MessageBody, MessageId, Timestamp}
import io.circe._
import io.circe.generic.semiauto._

sealed trait Message {
  def id: EventId
  def messageId: MessageId
  def created: Timestamp
  def body: MessageBody
}

object Message {
  final case class FromMessage(id: EventId, messageId: MessageId, body: MessageBody, created: Timestamp) extends Message
  final case class ToMessage(id: EventId, messageId: MessageId,  body: MessageBody, created: Timestamp)  extends Message

  implicit val eventEncoder: Encoder[Message] = deriveEncoder
  implicit val eventDecoder: Decoder[Message] = deriveDecoder
}
