package com.example.repositories

interface ICrudRepository<T, G> {
    suspend fun create(data: T): G
    suspend fun get(id: G): T?
    suspend fun getAll(): List<T>
    suspend fun update(data: T)
    suspend fun delete(id: G)
}