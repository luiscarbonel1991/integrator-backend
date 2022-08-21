package com.devhighlevel.shared.mappers

import com.devhighlevel.domain.user.User
import com.devhighlevel.shared.dto.UserCreateRequestDTO
import com.devhighlevel.shared.dto.UserResponseDTO

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