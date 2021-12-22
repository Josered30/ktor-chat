package com.example.controllers;


import com.example.models.resources.LoginInput
import com.example.models.resources.LoginOutput
import com.example.models.resources.TokenInput
import com.example.models.resources.RegisterInput
import com.example.services.JwtService
import com.example.services.UserService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoutes() {

    val userService: UserService by inject()
    val jwtService: JwtService by inject()

    route("/api/auth") {

        post("/register") {
            val registerInput = call.receive<RegisterInput>()
            // Check username and password
            // ...
            val result = userService.create(registerInput.email, registerInput.name, registerInput.password)
            if (!result.success) {
                return@post call.respondText(result.message, status = HttpStatusCode.BadRequest)
            }
            call.respond(result)
        }

        post("/login") {
            val loginInput = call.receive<LoginInput>()

            val token = jwtService.createTokenPair(loginInput.email, loginInput.password)

            if (!token.success) {
                call.respond(HttpStatusCode.OK, token)
            }
            call.respond(LoginOutput(token.userId, token.token, token.refreshToken))

        }

        post("/refresh") {
            val tokenInput = call.receive<TokenInput>()
            val token = jwtService.refreshTokenPair(tokenInput.userId, tokenInput.refreshToken)

            if (!token.success) {
                call.respond(HttpStatusCode.BadRequest, token)
            }
            call.respond(token)
        }
    }
}
