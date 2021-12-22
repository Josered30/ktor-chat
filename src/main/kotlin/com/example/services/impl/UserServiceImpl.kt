package com.example.services.impl

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.models.User
import com.example.models.resources.RegisterOutput
import com.example.models.resources.UserOutput
import com.example.repositories.UserRepository
import com.example.services.UserService

class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override suspend fun create(userEmail: String, userName: String, userPassword: String): RegisterOutput {
        val hashPassword = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(6, userPassword.toCharArray())
        val user = userRepository.getByEmail(userEmail)
        if (user != null) {
            val result = RegisterOutput()
            result.success = false
            result.message = "Already exist"
            return result
        }

        val id = userRepository.create(User(userEmail, userName, hashPassword))
        return RegisterOutput(id, userName, userEmail)
    }

    override suspend fun get(id: String): UserOutput? {
        val user = userRepository.get(id) ?: return null
        return UserOutput(user.id, user.email, user.name)

    }

    override suspend fun getByEmail(email: String): UserOutput? {
        val user = userRepository.getByEmail(email) ?: return null
        return UserOutput(user.id, user.email, user.name)
    }

    override suspend fun getAll(): List<User> {
        return userRepository.getAll()
    }
}