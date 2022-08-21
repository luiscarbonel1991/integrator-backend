package com.devhighlevel.domain.user.repository

import com.devhighlevel.domain.user.User

interface UserRepository {
    suspend fun findAll(): List<User>
    suspend fun save(user: User): User
    suspend fun update(user: User): User
    suspend fun delete(id: String)
    suspend fun findById(userId: String): User?
    suspend fun findByEmail(email: String): User?
}