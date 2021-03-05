package com.example.cleverhouse

import cats.effect.{Async, Sync}
import cats.syntax.semigroupk._
import cats.{Defer, Monad}
import com.example.cleverhouse.Main.Routes
import org.http4s.implicits._
import org.http4s.server.Router

object Routes {
  def make[Init[_] : Defer : Monad : Async](
   ): Routes[Init] = {

    Router("/api"-> (new SensorsRoutes[Init].routes <+> new ClientRoutes[Init].routes)).orNotFound
  }

}
