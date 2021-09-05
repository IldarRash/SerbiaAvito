package com.example.instagram

import cats.effect.Async
import com.example.instagram.config.DatabaseConfig
import cats.effect.{Async, ContextShift, Resource, Sync}
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import cats.syntax.functor._
import doobie.quill.DoobieContext
import io.getquill.{Literal, PostgresJdbcContext, SnakeCase}

import scala.concurrent.ExecutionContext

object Database {
  def dbTransactor[F[_]: Async: ContextShift](
                                               dbc: DatabaseConfig,
                                               connEc: ExecutionContext,
                                             ): Resource[F, HikariTransactor[F]] =
    HikariTransactor
      .newHikariTransactor[F](dbc.driver, dbc.url, dbc.user, dbc.password, connEc)

  /**
   * Runs the flyway migrations against the target database
   */
  def initializeDb[F[_]](cfg: DatabaseConfig)(implicit S: Sync[F]): F[Unit] =
    S.delay {
      val fw: Flyway =
        Flyway
          .configure()
          .dataSource(cfg.url, cfg.user, cfg.password)
          .load()
      fw.migrate()
    }.as(())
}

package object database {
  lazy val ctx = new DoobieContext.Postgres(SnakeCase)
}