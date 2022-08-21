package com.devhighlevel.domain.user.services

import com.devhighlevel.domain.user.User

interface UserService {
    suspend fun users(): List<User>
    suspend fun create(user: User): User
    suspend fun update(user: User): User
    suspend fun delete(userId: String)
    suspend fun findById(userId: String): User?
    suspend fun findByEmail(email: String): User?
}