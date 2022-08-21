package com.devhighlevel.application.services

import com.devhighlevel.domain.user.services.UserService
import com.devhighlevel.domain.user.User

import com.devhighlevel.shared.enums.Role
import com.devhighlevel.shared.dto.UserCreateRequestDTO
import com.devhighlevel.shared.dto.UserUpdateRequestDTO
import com.devhighlevel.shared.mappers.UserMapper
import io.ktor.server.plugins.*
import io.ktor.util.reflect.*
import java.util.*

class UserCommandHandler(private val userService: UserService) {

    suspend fun users() = userService.users()

    suspend fun create(userCreateRequestDTO: UserCreateRequestDTO): User {
        kotlin.runCatching {
            findByEmail(userCreateRequestDTO.email)
        }.onSuccess {
            throw BadRequestException("User with email: ${userCreateRequestDTO.email} already exist.")
        }.onFailure { }
        return userService.create(UserMapper.toCreateUser(userCreateRequestDTO))
    }

    suspend fun update(userUpdateRequestDTO: UserUpdateRequestDTO, id: String): User {
        val userFound = findById(id)
        userUpdateRequestDTO.email?.let { userFound.email = it }
        userUpdateRequestDTO.enabled?.let { userFound.enabled = it }
        userUpdateRequestDTO.image?.let { userFound.image = it }
        userUpdateRequestDTO.name?.let { userFound.name = it }
        userUpdateRequestDTO.password?.let { userFound.password = it }
        userUpdateRequestDTO.role?.let { userFound.role = it }
        return userService.update(userFound)
    }

    suspend fun update(user: User): User {
        return userService.update(user)
    }

    suspend fun enableOrDisable(userUpdateRequestDTO: UserUpdateRequestDTO, id: String): User {
        if (userUpdateRequestDTO.enabled != null) {
            val userFound = findById(id)
            userUpdateRequestDTO.enabled.let {
                if (it) userFound.attempts = 0 else userFound.attempts = 3
                userFound.enabled = it
                return userService.update(userFound)
            }
        }
        throw BadRequestException("Field 'enabled' is empty or null")
    }

    suspend fun updateRole(userUpdateRequestDTO: UserUpdateRequestDTO, id: String): User {
        if (userUpdateRequestDTO.role != null) {
            kotlin.runCatching {
                Role.of(userUpdateRequestDTO.role)
            }.onSuccess {
                val userFound = findById(id)
                userUpdateRequestDTO.role.let {
                    userFound.role = it
                    return userService.update(userFound)
                }
            }.onFailure {
                throw BadRequestException(it.message ?: "Role is invalid")
            }
        }
        throw BadRequestException("Field 'role' is empty or null")
    }

    suspend fun delete(userId: String): User {
        val userFound = findById(userId)
        userService.delete(userFound.id!!)
        return userFound
    }

    suspend fun deleteBatch(ids: List<String>): MutableMap<String, List<String>> {
        val success = mutableListOf<String>()
        val errors = mutableListOf<String>()
        val notFound = mutableListOf<String>()
        val results = mutableMapOf<String, List<String>>()
        val idsPartitioned = ids.distinct().partition { it.isNotBlank() }

        idsPartitioned.first.map { userId ->
            kotlin.runCatching {
                UUID.fromString(userId)
                delete(userId)
            }.onSuccess {
                success.add(userId)
            }.onFailure {
                when {
                    it.instanceOf(NotFoundException::class) -> notFound.add(userId)
                    else -> errors.add(userId)
                }
            }
        }
        results["success"] = success
        results["not_found"] = notFound
        results["not_valid"] = idsPartitioned.second.plus(errors)
        return results
    }

    suspend fun findByEmail(email: String) =
        userService.findByEmail(email) ?: throw NotFoundException("User with email $email not found")


    private suspend fun findById(userId: String) =
        userService.findById(userId) ?: throw NotFoundException("User with id: $userId not found")

}