package com.devhighlevel.db.repository

sealed interface GenericCrud<E> {
    suspend fun findAll(): List<E>
    suspend fun save(e: E): E
    suspend fun update(e: E): E
    suspend fun delete(id: String)
}