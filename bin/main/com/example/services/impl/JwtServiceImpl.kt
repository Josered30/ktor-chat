package com.example.services.impl

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.RefreshToken
import com.example.models.resources.TokenOutput
import com.example.repositories.RefreshTokenRepository
import com.example.repositories.UserRepository
import com.example.services.JwtService
import io.ktor.application.*
import java.time.Duration
import java.util.*

class JwtServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository,
    private val environment: ApplicationEnvironment
) :
    JwtService {

    override suspend fun createTokenPair(userEmail: String, userPassword: String): TokenOutput {
        val user = userRepository.getByEmail(userEmail)
        if (user == null) {
            val tokenOutput = TokenOutput()
            tokenOutput.success = false
            tokenOutput.message = "User not exist"
            return tokenOutput
        }

        val result: BCrypt.Result = BCrypt.verifyer().verify(userPassword.toCharArray(), user.password)
        if (!result.verified) {
            val tokenOutput = TokenOutput()
            tokenOutput.success = false
            tokenOutput.message = "Password Error"
            return tokenOutput
        }

        val currentTime = System.currentTimeMillis()

        val jwt = createToken(userEmail)
        val refreshToken = UUID.randomUUID().toString()
        val expiresAt = currentTime + Duration.ofDays(10).toMillis()

        refreshTokenRepository.create(
            RefreshToken(
                user.id,
                refreshToken,
                expiresAt
            )
        )
        return TokenOutput(jwt, refreshToken)
    }

    override suspend fun refreshTokenPair(userId: String, refreshToken: String): TokenOutput {
        val currentTime = System.currentTimeMillis()
        val oldRefresh = refreshTokenRepository.getByRefreshToken(refreshToken)
        val user = userRepository.get(userId)

        if (user == null) {
            val tokenOutput = TokenOutput()
            tokenOutput.success = false
            tokenOutput.message = "User Error"
            return tokenOutput
        }
        if (oldRefresh == null) {
            val tokenOutput = TokenOutput()
            tokenOutput.success = false
            tokenOutput.message = "Refresh Error"
            return tokenOutput
        }
        if (oldRefresh.expiresAt > currentTime) {
            val tokenOutput = TokenOutput()
            tokenOutput.success = false
            tokenOutput.message = "Refresh time error"
            return tokenOutput
        }

        val jwt = createToken(user.email)

        val newRefreshToken = UUID.randomUUID().toString()
        val expiresAt = currentTime + Duration.ofDays(10).toMillis()

        val newRefresh = RefreshToken(oldRefresh.id, oldRefresh.userId, newRefreshToken, expiresAt)
        refreshTokenRepository.update(newRefresh)
        return TokenOutput(jwt, refreshToken)
    }

    private fun createToken(subject: String): String {
        val currentTime = System.currentTimeMillis()
        val jwtAudience = environment.config.property("jwt.audience").getString()
        val jwtIssuer = environment.config.property("jwt.domain").getString()
        val jwtSecret = environment.config.property("jwt.secret").getString()

        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withSubject(subject)
            .withExpiresAt(Date(currentTime + Duration.ofHours(1).toMillis()))
            .sign(Algorithm.HMAC256(jwtSecret))
    }

}