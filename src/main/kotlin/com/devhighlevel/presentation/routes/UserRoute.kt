package com.devhighlevel.presentation.routes

import com.devhighlevel.shared.dto.UserBatchDeleteDTO
import com.devhighlevel.shared.dto.UserCreateRequestDTO
import com.devhighlevel.shared.dto.UserDeleteBatchResponseDTO
import com.devhighlevel.shared.dto.UserUpdateRequestDTO
import com.devhighlevel.shared.mappers.UserMapper
import com.devhighlevel.application.services.UserCommandHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.usersRoute() {

    val userCommandHandler: UserCommandHandler by inject()

    val userIdEmptyMessage = "User id is empty or null"
    route("/users") {
        get { call.respond(userCommandHandler.users().map { UserMapper.toUserResponseDTO(it) }) }

        post {
            val requestDTO = call.receive<UserCreateRequestDTO>()
            call.respond(
                HttpStatusCode.Created,
                UserMapper.toUserResponseDTO(userCommandHandler.create(requestDTO))
            )
        }

        put("/{id}") {
            val userId = call.parameters["id"] ?: throw BadRequestException(userIdEmptyMessage)
            val requestDTO = call.receive<UserUpdateRequestDTO>()
            call.respond(HttpStatusCode.OK, UserMapper.toUserResponseDTO(userCommandHandler.update(requestDTO, userId)))
        }

        put("/enable/{id}") {
            val userId = call.parameters["id"] ?: throw BadRequestException(userIdEmptyMessage)
            val requestDTO = call.receive<UserUpdateRequestDTO>()
            call.respond(HttpStatusCode.OK, UserMapper.toUserResponseDTO(userCommandHandler.enableOrDisable(requestDTO, userId)))
        }

        put ("/roles/{id}"){
            val userId = call.parameters["id"] ?: throw BadRequestException(userIdEmptyMessage)
            val requestDTO = call.receive<UserUpdateRequestDTO>()
            call.respond(HttpStatusCode.OK, UserMapper.toUserResponseDTO(userCommandHandler.updateRole(requestDTO, userId)))
        }

        patch ("/batch-delete"){
            val request = call.receive<UserBatchDeleteDTO>()
            call.respond(HttpStatusCode.OK, UserDeleteBatchResponseDTO(userCommandHandler.deleteBatch(request.ids)))
        }

        delete("/{id}") {
            val userId = call.parameters["id"] ?: throw BadRequestException(userIdEmptyMessage)
            call.respond(HttpStatusCode.OK, UserMapper.toUserResponseDTO(userCommandHandler.delete(userId)))
        }
    }
}