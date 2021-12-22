package com.example.database

import org.jetbrains.exposed.dao.id.UUIDTable

object UserTable : UUIDTable() {
    val email = varchar("email", 100)
    val password = varchar("password", 100)
    val name = varchar("name", 100)
}