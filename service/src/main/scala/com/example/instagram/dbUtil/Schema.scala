package com.example.instagram.dbUtil

import com.example.instagram.Message.{InstagramMessage}
import doobie.quill.DoobieContext.Postgres
import io.getquill.{EntityQuery, MappedEncoding, SnakeCase}

import java.time.Instant
import java.util.Date

trait Schema {
  val ctx: Postgres[SnakeCase] with Decoders with Encoders

  import ctx._
  implicit val messageMeta = insertMeta[InstagramMessage](_.id)
  /*val messageSchema: Quoted[EntityQuery[Message]] = quote {
    querySchema[Message](
      "Message",
      _.id -> "messageId",
      _.id -> "eventId",
      _.body -> "body",
      _.from -> "from",
      _.created -> "time"
    )

  }*/

}

trait Decoders {
  implicit val instantDecoder: MappedEncoding[Date, Instant] = MappedEncoding[Date, Instant](_.toInstant)
}

trait Encoders {
  implicit val instanceEncoder: MappedEncoding[Instant, Date] = MappedEncoding[Instant, Date](Date.from)
}