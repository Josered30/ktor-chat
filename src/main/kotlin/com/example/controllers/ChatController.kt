package com.example.controllers

import com.example.models.resources.ChatMessageOutput
import com.example.plugins.AUTH_JWT
import com.example.websocket.ChatConnection
import com.google.gson.Gson
import io.ktor.auth.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.LinkedHashSet


fun Route.chatRoutes() {
    val gson = Gson()

    route("/api/chat") {
        authenticate(AUTH_JWT) {

            val connections = Collections.synchronizedSet<ChatConnection?>(LinkedHashSet())
            webSocket {
                val thisConnection = ChatConnection(this)
                connections += thisConnection
                try {
                    val initial =
                        ChatMessageOutput(
                            "You are connected! There are ${connections.count()} users here.",
                            System.currentTimeMillis(),
                            "Info",
                            ""
                        )
                    send(gson.toJson(initial))

                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        connections.forEach {
                            it.session.send(receivedText)
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    println("Removing $thisConnection!")
                    connections -= thisConnection
                }
            }
        }
    }
}