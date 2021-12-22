package com.example.services.impl

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.models.User
import com.example.models.resources.UserOutput
import com.example.repositories.UserRepository
import com.example.services.UserService

class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override suspend fun create(userEmail: String, userPassword: String): UserOutput {
        val hashPassword = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(6, userPassword.toCharArray())
        val user = userRepository.getByEmail(userEmail)
        if (user != null) {
            val result = UserOutput()
            result.success = false
            result.message = "Already exist"
            return result
        }

        val id = userRepository.create(User(userEmail, hashPassword))
        return UserOutput(id, userEmail)
    }

    override suspend fun get(id: String): User? {
        return userRepository.get(id)
    }

    override suspend fun getByEmail(email: String): User? {
        return userRepository.getByEmail(email)
    }

    override suspend fun getAll(): List<User> {
        return userRepository.getAll()
    }
}