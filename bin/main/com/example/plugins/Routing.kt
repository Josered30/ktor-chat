package com.example.plugins

import com.example.controllers.authRoutes
import com.example.controllers.userRoutes
import com.example.repositories.UserRepository
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {
    install(Routing) {
        authRoutes()
        userRoutes()
    }
}
