package com.devhighlevel.domain.user.services

import com.devhighlevel.domain.user.User
import com.devhighlevel.domain.user.repository.UserRepository

class UserDomainService(private val userRepository: UserRepository): UserService {
    override suspend fun users(): List<User> = userRepository.findAll()

    override suspend fun create(user: User): User = userRepository.save(user)

    override suspend fun update(user: User): User = userRepository.update(user)

    override suspend fun delete(userId: String) = userRepository.delete(userId)

    override suspend fun findById(userId: String): User? = userRepository.findById(userId)

    override suspend fun findByEmail(email: String): User? = userRepository.findByEmail(email)
}