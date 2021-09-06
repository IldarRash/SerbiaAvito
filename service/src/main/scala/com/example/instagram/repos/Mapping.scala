package com.example.instagram.repos

import doobie.Get

import java.time.Instant
import doobie.enumerated.SqlState
import doobie.util.meta.Meta
import doobie.implicits.javasql._
import doobie.Put

import java.util.UUID
trait Mapping {
  val UNIQUE_VIOLATION: SqlState =
    doobie.postgres.sqlstate.class23.UNIQUE_VIOLATION

  implicit val DateTimeMeta: Meta[Instant] =
    Meta[java.sql.Timestamp].imap(_.toInstant)(java.sql.Timestamp.from)

  implicit val uuidMeta: Meta[UUID] =
    Meta[String].imap[UUID](UUID.fromString)(_.toString)
}

