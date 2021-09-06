package com.example.instagram.repos

import cats.data.OptionT
import com.example.instagram
import com.example.instagram.Message.InstagramMessage

import java.util.UUID


trait MessageRepo[F[_]] {
  def addMessage(message: InstagramMessage): F[Int]
  def all: fs2.Stream[F, InstagramMessage]
  def findIsFrom(isFrom: Boolean): fs2.Stream[F, InstagramMessage]
  /*def byId(id: UUID): OptionT[F, InstagramMessage]*/
}

