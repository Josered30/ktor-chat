package com.example.repositories

import com.example.database.DatabaseFactory
import com.example.database.RefreshTokenTable
import com.example.models.RefreshToken
import org.jetbrains.exposed.sql.*
import java.util.*

class RefreshTokenRepository : ICrudRepository<RefreshToken, String> {
    override suspend fun create(data: RefreshToken): String {

        val id = DatabaseFactory.dbQuery {
            RefreshTokenTable.insertAndGetId {
                it[refreshToken] = data.refreshToken
                it[userId] = UUID.fromString(data.userId)
                it[expiresAt] = data.expiresAt
            }
        }
        return id.toString()
    }

    override suspend fun get(id: String): RefreshToken? {
        return DatabaseFactory.dbQuery {
            RefreshTokenTable.select { RefreshTokenTable.id eq UUID.fromString(id) }.map {
                it.toRefreshToken()
            }.firstOrNull()
        }
    }

    override suspend fun getAll(): List<RefreshToken> {
        return DatabaseFactory.dbQuery {
            RefreshTokenTable.selectAll().map { it.toRefreshToken() }
        }
    }

    override suspend fun update(data: RefreshToken) {
        DatabaseFactory.dbQuery {
            RefreshTokenTable.update({ RefreshTokenTable.id eq UUID.fromString(data.id) }) {
                it[refreshToken] = data.refreshToken
                it[expiresAt] = data.expiresAt
            }
        }
    }

    override suspend fun delete(id: String) {
        TODO("Not yet implemented")
    }

    suspend fun getByUserId(userId: String): RefreshToken? {
        return DatabaseFactory.dbQuery {
            RefreshTokenTable.select { RefreshTokenTable.userId eq UUID.fromString(userId) }.map {
                it.toRefreshToken()
            }.firstOrNull()
        }
    }

    suspend fun getByRefreshToken(refreshToken: String): RefreshToken? {
        return DatabaseFactory.dbQuery {
            RefreshTokenTable.select { RefreshTokenTable.refreshToken eq refreshToken }.map {
                it.toRefreshToken()
            }.firstOrNull()
        }
    }

    private fun ResultRow.toRefreshToken(): RefreshToken {
        return RefreshToken(
            this[RefreshTokenTable.id].toString(),
            this[RefreshTokenTable.userId].toString(),
            this[RefreshTokenTable.refreshToken],
            this[RefreshTokenTable.expiresAt]
        )
    }
}