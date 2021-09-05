package com.example.instagram

object config {
  case class AppConfig(apiHost: String, apiPort: Int)

  case class DatabaseConfig(
                             url: String,
                             driver: String,
                             user: String,
                             password: String
                           )
}
