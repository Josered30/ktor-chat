package com.example.models.resources


data class ChatMessageOutput(
    val content: String,
    val timestamp: Long,
    val senderName: String,
    val senderId: String
)