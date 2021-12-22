package com.example.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object RefreshTokenTable : UUIDTable("refresh_tokens") {
    val userId: Column<UUID> = uuid("userId").references(UserTable.id)
    val refreshToken = varchar("refreshToken", 300)
    val expiresAt = long("expiresAt")
}