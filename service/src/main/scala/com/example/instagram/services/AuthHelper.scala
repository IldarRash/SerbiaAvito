package com.example.instagram.services

import com.example.instagram.{User, UserResponse}
import com.example.instagram.config.JwtConfig
import io.circe.syntax.EncoderOps
import pdi.jwt.{Jwt, JwtAlgorithm}

class AuthHelper(jwtConfig: JwtConfig) {

  def encode(user: User): UserResponse = UserResponse(user, Jwt.encode(user.asJson.toString(), jwtConfig.hmacSecret, JwtAlgorithm.HS256))

}
