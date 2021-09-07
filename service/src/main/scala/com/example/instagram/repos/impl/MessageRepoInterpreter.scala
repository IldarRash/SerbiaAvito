package com.example.instagram.repos.impl

import cats.effect.Async
import com.example.instagram.Message.InstagramMessage
import com.example.instagram.repos.{Mapping, MessageRepo}
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.postgres._
import doobie.postgres.implicits._



class MessageRepoInterpreter [F[_]: Async](xa: Transactor[F]) extends MessageRepo[F] with Mapping {

  override def addMessage(message: InstagramMessage): F[Int] =
      MessageRepoInterpreter
        .addMessage(message)
        .run
        .transact(xa)

  override def all: fs2.Stream[F, InstagramMessage] =
      MessageRepoInterpreter
        .all
        .stream
        .transact(xa)


  override def findIsFrom(isFrom: Boolean): fs2.Stream[F, InstagramMessage] =
      MessageRepoInterpreter
        .findIsFrom(isFrom)
        .stream
        .transact(xa)

/*  override def byId(id: UUID): F[Option[InstagramMessage]]=
    run(query[InstagramMessage].filter(_.id == lift(id)).take(1)).transact(xa)*/
}


object MessageRepoInterpreter {
  def apply[F[_]: Async](xa: Transactor[F]
                               ): MessageRepoInterpreter[F] =
    new MessageRepoInterpreter[F](xa)


  def addMessage(message: InstagramMessage): doobie.Update0 =
    sql"""
         |INSERT INTO message (
         |  message_id,
         |  event_id,
         |  body,
         |  isFrom,
         |  created
         |)
         |VALUES (
         |  ${message.id},
         |  ${message.eventId},
         |  ${message.body},
         |  ${message.isFrom},
         |  ${message.created}
         |)
     """.stripMargin
      .update

  def all: doobie.Query0[InstagramMessage] =
    sql"""
         |SELECT * FROM message
       """.stripMargin
      .query[InstagramMessage]

  def findIsFrom(isFrom: Boolean): doobie.Query0[InstagramMessage] =
    sql"""
         |SELECT * FROM message m
         |WHERE m.isFrom = ${isFrom}
       """.stripMargin
      .query[InstagramMessage]
}