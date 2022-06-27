package com.devhighlevel.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.statusPages() {
    install(StatusPages) {
        exception<BadRequestException> { call, throwable ->
            respond(
                call,
                HttpStatusCode.BadRequest,
                throwable.message,
                throwable
            )
        }

        exception<NotFoundException> { call, throwable ->
            respond(call, HttpStatusCode.NotFound, throwable.message, throwable)
        }

        exception<AuthorizationException> { call, throwable ->
            respond(call, HttpStatusCode.Forbidden, HttpStatusCode.Forbidden.description, throwable)
        }

        exception<AuthenticationException> { call, throwable ->
            println(throwable.message)
            respond(call, HttpStatusCode.Unauthorized, HttpStatusCode.Unauthorized.description, throwable)
        }

        exception<Throwable> { call, throwable ->
            respond(call, HttpStatusCode.InternalServerError, "Something worn!!!", throwable)
        }
    }
}

suspend fun respond(call: ApplicationCall, httpStatusCode: HttpStatusCode, messageCause: String?, throwable: Throwable) {
    println(throwable.message)
    call.respond(httpStatusCode, mapOf("messages" to (messageCause ?: httpStatusCode.description)))
}