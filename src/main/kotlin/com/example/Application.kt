package com.example

import com.example.database.DatabaseFactory
import io.ktor.application.*
import com.example.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DatabaseFactory.init()

    configureKoin(environment)
    configureSecurity()
    configureWebSockets()
    configureRouting()
    configureHTTP()
    configureSerialization()

}
