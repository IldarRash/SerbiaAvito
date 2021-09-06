package com.example.instagram

import cats.Parallel
import cats.effect.{Blocker, Concurrent, ConcurrentEffect, ContextShift, Resource, Timer}
import cats.implicits.catsSyntaxApplicativeId
import com.example.instagram.Main.F
import com.example.instagram.config.{DatabaseConfig, ServerConfig}
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import org.http4s.HttpApp
import pureconfig.ConfigSource
import pureconfig.generic.auto._


final case class Resources[F[_]](config: ServerConfig, xa: HikariTransactor[F])

object Resources {
  def make[
    F[_] : Concurrent : ContextShift : Parallel : Logger : Timer
  ]: Resource[F, Resources[F]] =
    for {
      serverConfig <- Resource.eval(ConfigSource.default.at("server").loadOrThrow[ServerConfig].pure[F])
      dataBaseConfig <- Resource.eval(ConfigSource.default.at("database").loadOrThrow[DatabaseConfig].pure[F])
      connEc <- ExecutionContexts.fixedThreadPool[F](dataBaseConfig.poolSize)
      blocker <- Blocker[F]
      xa <- Database.dbTransactor(dataBaseConfig, connEc, blocker)

    } yield Resources(serverConfig, xa)
}