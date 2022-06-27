package com.devhighlevel.routes

import com.devhighlevel.dto.UserBatchDeleteDTO
import com.devhighlevel.dto.UserCreateRequestDTO
import com.devhighlevel.dto.UserDeleteBatchResponseDTO
import com.devhighlevel.dto.UserUpdateRequestDTO
import com.devhighlevel.mappers.UserMapper
import com.devhighlevel.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.usersRoute() {

    val userService: UserService by inject()

    route("/users") {
        get { call.respond(userService.users().map { UserMapper.toUserResponseDTO(it) }) }

        post {
            val requestDTO = call.receive<UserCreateRequestDTO>()
            call.respond(
                HttpStatusCode.Created,
                UserMapper.toUserResponseDTO(userService.create(requestDTO))
            )
        }

        put("/{id}") {
            val userId = call.parameters["id"] ?: throw BadRequestException("User id is empty or null")
            val requestDTO = call.receive<UserUpdateRequestDTO>()
            call.respond(HttpStatusCode.OK, UserMapper.toUserResponseDTO(userService.update(requestDTO, userId)))
        }

        put("/enable/{id}") {
            val userId = call.parameters["id"] ?: throw BadRequestException("User id is empty or null")
            val requestDTO = call.receive<UserUpdateRequestDTO>()
            call.respond(HttpStatusCode.OK, UserMapper.toUserResponseDTO(userService.enableOrDisable(requestDTO, userId)))
        }

        put ("/roles/{id}"){
            val userId = call.parameters["id"] ?: throw BadRequestException("User id is empty or null")
            val requestDTO = call.receive<UserUpdateRequestDTO>()
            call.respond(HttpStatusCode.OK, UserMapper.toUserResponseDTO(userService.updateRole(requestDTO, userId)))
        }

        patch ("/batch-delete"){
            val request = call.receive<UserBatchDeleteDTO>()
            call.respond(HttpStatusCode.OK, UserDeleteBatchResponseDTO(userService.deleteBatch(request.ids)))
        }

        delete("/{id}") {
            val userId = call.parameters["id"] ?: throw BadRequestException("User id is empty or null")
            call.respond(HttpStatusCode.OK, UserMapper.toUserResponseDTO(userService.delete(userId)))
        }
    }
}