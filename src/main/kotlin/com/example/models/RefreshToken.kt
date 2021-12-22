package com.example.models


data class RefreshToken(val id: String, val userId: String, val refreshToken: String, val expiresAt: Long) {
    constructor(userId: String, refreshToken: String, expiresAt: Long) : this(
        "",
        userId,
        refreshToken,
        expiresAt
    )
}