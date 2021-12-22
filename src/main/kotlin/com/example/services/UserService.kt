package com.example.services

import com.example.models.User
import com.example.models.resources.RegisterOutput
import com.example.models.resources.UserOutput

interface UserService {
    suspend fun create(userEmail: String, userName: String, userPassword: String): RegisterOutput
    suspend fun get(id: String): UserOutput?
    suspend fun getByEmail(email: String): UserOutput?
    suspend fun getAll(): List<User>
}