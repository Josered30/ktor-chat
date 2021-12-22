package com.example.repositories

import com.example.database.DatabaseFactory.dbQuery
import com.example.database.RefreshTokenTable
import com.example.database.UserTable
import com.example.models.User
import org.jetbrains.exposed.sql.*
import java.util.*

class UserRepository : ICrudRepository<User, String> {

    override suspend fun create(data: User): String {
        val id = dbQuery {
            UserTable.insertAndGetId {
                it[email] = data.email
                it[password] = data.password
                it[name] = data.name
            }
        }
        return id.toString()
    }

    override suspend fun get(id: String): User? {
        return dbQuery {
            UserTable.select { UserTable.id eq UUID.fromString(id) }.map {
                it.toUser()
            }.firstOrNull()
        }
    }

    override suspend fun getAll(): List<User> {
        return dbQuery {
            UserTable.selectAll().map { it.toUser() }
        }
    }

    override suspend fun update(data: User) {
        dbQuery {
            UserTable.update({ UserTable.id eq UUID.fromString(data.id) }) {
                it[email] = data.email
            }
        }
    }

    override suspend fun delete(id: String) {
        TODO("Not yet implemented")
    }

    suspend fun getByEmail(email: String): User? {
        return dbQuery {
            UserTable.select { UserTable.email eq email }.map {
                it.toUser()
            }.firstOrNull()
        }
    }

    private fun ResultRow.toUser(): User {

        return User(
            this[UserTable.id].toString(),
            this[UserTable.email],
            this[UserTable.name],
            this[UserTable.password]
        )
    }
}