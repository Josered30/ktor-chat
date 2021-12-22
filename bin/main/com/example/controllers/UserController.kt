package com.example.controllers

import com.example.services.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject



fun Route.userRoutes() {

    val userService: UserService by inject()

    route("/api/user") {
        authenticate("auth-jwt") {
            get("/{id}") {
                val id =
                        call.parameters["id"]
                                ?: return@get call.respondText(
                                        "Bad Request",
                                        status = HttpStatusCode.BadRequest
                                )
                //                val principal = call.principal<JWTPrincipal>()
                //                val email = principal!!.payload.getClaim("email").asString()

                val user =
                        userService.get(id)
                                ?: return@get call.respondText(
                                        "Not found",
                                        status = HttpStatusCode.NotFound
                                )
                call.respond(user)
            }
        }
    }
}
