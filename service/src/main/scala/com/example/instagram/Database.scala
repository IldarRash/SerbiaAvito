package com.example.instagram


import cats.effect.{Async, Blocker, ContextShift, Resource, Sync}
import cats.syntax.functor._
import com.example.instagram.config.DatabaseConfig
import doobie.hikari.HikariTransactor
import doobie.quill.DoobieContext
import io.getquill.SnakeCase
import org.flywaydb.core.Flyway

import scala.concurrent.ExecutionContext

object Database {
  def dbTransactor[F[_]: Async: ContextShift](dbc: DatabaseConfig, connEc: ExecutionContext, blocker: Blocker): Resource[F, HikariTransactor[F]] =
    HikariTransactor
      .newHikariTransactor[F](dbc.driver, dbc.url, dbc.user, dbc.password, connEc, blocker)

  /**
   * Runs the flyway migrations against the target database
   */
  def initializeDb[F[_]](cfg: DatabaseConfig)(implicit S: Sync[F]): F[Unit] =
    S.delay {
      val fw =
        Flyway
          .configure(this.getClass.getClassLoader)
          .locations("db.migration")
          .dataSource(cfg.url, cfg.user, cfg.password)
          .load()
      fw.migrate()
    }.as(())
}

package object database {
  lazy val ctx = new DoobieContext.Postgres(SnakeCase)
}