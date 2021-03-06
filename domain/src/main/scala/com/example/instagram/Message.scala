package com.example.instagram

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

import java.time.Instant
import java.util.UUID

sealed trait Message {
  def id: UUID
  def eventId: UUID
  def body: String
  def isFrom: Boolean
  def created: Instant
}

object Message {
  final case class InstagramMessage(id: UUID, eventId: UUID, body: String, isFrom: Boolean, created: Instant) extends Message


  implicit val messageToEncoder: Encoder[Message] = deriveEncoder
  implicit val messageToDecoder: Decoder[Message] = deriveDecoder

  implicit val messageFromEncoder: Encoder[Message] = deriveEncoder
  implicit val messageFromDecoder: Decoder[Message] = deriveDecoder
}
