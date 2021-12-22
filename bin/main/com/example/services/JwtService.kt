package com.example.services

import com.example.models.RefreshToken
import com.example.models.User
import com.example.models.resources.TokenOutput

interface JwtService {
    suspend fun createTokenPair(userEmail: String, userPassword: String): TokenOutput
    suspend fun refreshTokenPair(userId: String, refreshToken: String): TokenOutput
}