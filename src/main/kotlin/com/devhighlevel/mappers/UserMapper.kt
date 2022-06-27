package com.devhighlevel.mappers

import com.devhighlevel.db.documents.User
import com.devhighlevel.dto.UserCreateRequestDTO
import com.devhighlevel.dto.UserResponseDTO

object UserMapper {

    fun toUserResponseDTO(user: User) = UserResponseDTO(
        user.email,
        user.name,
        user.role,
        user.image
    )

    fun toCreateUser(userCreateRequestDTO: UserCreateRequestDTO) = User(
       null,
        userCreateRequestDTO.email,
        userCreateRequestDTO.password,
        userCreateRequestDTO.name,
        userCreateRequestDTO.role,
        userCreateRequestDTO.image,
    )

}