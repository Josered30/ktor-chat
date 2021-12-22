package com.example.controllers;


import com.example.models.resources.TokenInput
import com.example.models.resources.UserInput
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
            val userInput = call.receive<UserInput>()
            // Check username and password
            // ...
            val result = userService.create(userInput.email, userInput.password)
            if (!result.success) {
                return@post call.respondText(result.message, status = HttpStatusCode.BadRequest)
            }
            call.respondText("Registered")
        }

        post("/login") {
            val userInput = call.receive<UserInput>()
            val token = jwtService.createTokenPair(userInput.email, userInput.password)

            if (!token.success) {
                call.respond(HttpStatusCode.BadRequest, token)
            }
            call.respond(token)
        }

        post("/refresh") {
            val tokenInput = call.receive<TokenInput>()
            val token = jwtService.refreshTokenPair(tokenInput.userId, tokenInput.refreshToken  )

            if (!token.success) {
                call.respond(HttpStatusCode.BadRequest, token)
            }
            call.respond(token)
        }
    }
}
