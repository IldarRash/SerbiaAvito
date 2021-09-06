package com.example.instagram.services

import cats.effect.Async
import com.example.instagram.Message.InstagramMessage
import com.example.instagram.repos.MessageRepo

import java.util.UUID

class MessageService [F[_]: Async](messageRepo: MessageRepo[F]){
  def addMessage(message: InstagramMessage): F[UUID] =
    messageRepo.addMessage(message)

  def all: F[List[InstagramMessage]] =
      messageRepo.all
  def byFrom(isFrom: Boolean):F[List[InstagramMessage]] =
      messageRepo.findIsFrom(isFrom)
/*  def byId(id: UUID): OptionT[F, InstagramMessage] =
      messageRepo.byId(id)*/
}
object MessageService {
  def apply[F[_]: Async](messageRepo: MessageRepo[F]): MessageService[F] =
    new MessageService[F](messageRepo)
}