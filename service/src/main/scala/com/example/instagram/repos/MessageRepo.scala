package com.example.instagram.repos

import cats.effect.IO
import com.example.instagram.{EventId, Message}


object MessageRepo {

  trait Service[F[_]] {
    def all: List[Message]
    def byFrom(from: Boolean): F[List[Message]]
    def byId(id: EventId): F[Option[Message]]
  }
}

