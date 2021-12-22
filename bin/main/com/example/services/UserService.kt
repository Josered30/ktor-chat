package com.example.services

import com.example.models.User
import com.example.models.resources.UserOutput

interface UserService {
    suspend fun create(userEmail: String, userPassword: String): UserOutput
    suspend fun get(id: String): User?
    suspend fun getByEmail(email: String): User?
    suspend fun getAll(): List<User>
}