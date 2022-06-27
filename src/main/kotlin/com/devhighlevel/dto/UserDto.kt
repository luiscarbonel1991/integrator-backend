package com.devhighlevel.dto

data class UserCreateRequestDTO(
    val email: String,
    val password: String,
    val name: String,
    val role: String,
    val image: String?
)

data class UserUpdateRequestDTO(
    val email: String? = null,
    val name: String? = null,
    val role: String? = null,
    val image: String? = null,
    val enabled: Boolean? = null,
    val password: String? = null,
)

data class UserBatchDeleteDTO(
    val ids: List<String>
)

data class UserDeleteBatchResponseDTO(val results: Map<String, List<String>>)

data class UserResponseDTO(
    val email: String?,
    val name: String?,
    val role: String?,
    val image: String?
)

