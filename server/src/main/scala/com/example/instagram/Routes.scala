package com.example.instagram

import cats.effect.{Async, Sync}
import cats.syntax.semigroupk._
import cats.{Defer, Monad}
import com.example.instagram.Main.Routes
import org.http4s.implicits._
import org.http4s.server.Router

object Routes {
  def make[Init[_] : Defer : Monad : Async](
   ): Routes[Init] = {

    Router("/api"-> (new MessageRoutes[Init].routes)).orNotFound
  }

}
