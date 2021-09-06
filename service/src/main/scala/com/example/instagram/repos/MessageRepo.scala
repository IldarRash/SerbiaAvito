package com.example.instagram.repos

import cats.data.OptionT
import com.example.instagram
import com.example.instagram.Message.InstagramMessage

import java.util.UUID


trait MessageRepo[F[_]] {
  def addMessage(message: InstagramMessage): F[UUID]
  def all: F[List[InstagramMessage]]
  def findIsFrom(isFrom: Boolean): F[List[InstagramMessage]]
  /*def byId(id: UUID): OptionT[F, InstagramMessage]*/
}

