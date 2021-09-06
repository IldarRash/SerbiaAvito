package com.example.instagram.services

import cats.effect.Async
import cats.implicits._
import com.example.instagram.Logger
import com.example.instagram.Message.InstagramMessage
import com.example.instagram.repos.MessageRepo

import java.util.UUID

class MessageService [F[_]: Async : Logger](messageRepo: MessageRepo[F]){
  def addMessage(message: InstagramMessage): F[Int] =
    F.info(s"Add new message $message") *> messageRepo.addMessage(message)

  def all: fs2.Stream[F, InstagramMessage] =
      messageRepo.all

  def byFrom(isFrom: Boolean): fs2.Stream[F, InstagramMessage] =
      messageRepo.findIsFrom(isFrom)
/*  def byId(id: UUID): OptionT[F, InstagramMessage] =
      messageRepo.byId(id)*/
}
object MessageService {
  def apply[F[_]: Async : Logger](messageRepo: MessageRepo[F]): MessageService[F] =
    new MessageService[F](messageRepo)
}