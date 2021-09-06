package com.example.instagram.repos.impl

import cats.data.OptionT
import cats.effect.Async
import com.example.instagram.Message.InstagramMessage
import com.example.instagram.dbUtil.{Decoders, Encoders, Schema}
import com.example.instagram.repos.MessageRepo
import com.typesafe.scalalogging.{LazyLogging, Logger}
import doobie.implicits._
import doobie.quill.DoobieContext.Postgres
import doobie.util.transactor.Transactor
import io.getquill.SnakeCase

import java.util.UUID


class MessageRepoInterpreter [F[_]: Async](
         xa: Transactor[F],
         override val ctx: Postgres[SnakeCase] with Decoders with Encoders
            ) extends MessageRepo[F] with Schema {
  import ctx._

  override def addMessage(message: InstagramMessage): F[UUID] =run(quote {
    query[InstagramMessage].insert(lift(message)).returning(_.id)
  }).transact(xa)

  override def all: F[List[InstagramMessage]] = {
    for {
      list <- run(
        query[InstagramMessage]
      )
    } yield list
  }.transact(xa)


  override def findIsFrom(isFrom: Boolean): F[List[InstagramMessage]] = {
    for {
      list <- run(
        query[InstagramMessage]
          .filter(_.isFrom == lift(isFrom))
      )
    } yield list
  }.transact(xa)

/*  override def byId(id: UUID): F[Option[InstagramMessage]]=
    run(query[InstagramMessage].filter(_.id == lift(id)).take(1)).transact(xa)*/
}


object MessageRepoInterpreter {
  def apply[F[_]: Async](xa: Transactor[F], ctx: Postgres[SnakeCase] with Decoders with Encoders
                               ): MessageRepoInterpreter[F] =
    new MessageRepoInterpreter[F](xa, ctx)
}