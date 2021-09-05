package com.example.instagram

import com.example.instagram.{EventId, MessageBody, MessageId, Timestamp}
import io.circe._
import io.circe.generic.semiauto._

sealed trait Message {
  def id: EventId
  def messageId: MessageId
  def created: Timestamp
  def body: MessageBody
  def from: Boolean
}

object Message {
  final case class FromMessage(id: EventId, messageId: MessageId, body: MessageBody, created: Timestamp, from: Boolean) extends Message
  final case class ToMessage(id: EventId, messageId: MessageId,  body: MessageBody, created: Timestamp, from: Boolean)  extends Message

  implicit val eventEncoder: Encoder[Message] = deriveEncoder
  implicit val eventDecoder: Decoder[Message] = deriveDecoder
}
