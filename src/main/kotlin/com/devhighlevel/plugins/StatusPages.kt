package com.devhighlevel.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("StatusPages")
fun Application.statusPages() {
    install(StatusPages) {
        exception<BadRequestException> { call, throwable ->
            respond(
                call,
                HttpStatusCode.BadRequest,
                HttpStatusCode.BadRequest.description,
                throwable,
                throwable.message
            )
        }

        exception<NotFoundException> { call, throwable ->
            respond(call, HttpStatusCode.NotFound, HttpStatusCode.NotFound.description, throwable, throwable.message)
        }

        exception<AuthorizationException> { call, throwable ->
            respond(call, HttpStatusCode.Forbidden, HttpStatusCode.Forbidden.description, throwable, throwable.message)
        }

        exception<AuthenticationException> { call, throwable ->
            respond(
                call,
                HttpStatusCode.Unauthorized,
                HttpStatusCode.Unauthorized.description,
                throwable,
                throwable.message
            )
        }

        exception<Throwable> { call, throwable ->
            respond(call, HttpStatusCode.InternalServerError,HttpStatusCode.InternalServerError.description,  throwable, "Something worn!!!")
        }
    }
}

suspend fun respond(call: ApplicationCall, httpStatusCode: HttpStatusCode, code: String? = null, throwable: Throwable, messageCause: String? = null) {
   val message =IntegratorErrorResponse(httpStatusCode.value, code ?: httpStatusCode.description, messageCause?.let { listOf(it) } )
    logger.error("Request error: Status${httpStatusCode.value}, URL: ${call.request.uri}, Description: $message, Trace: ${throwable.message}")
    call.respond(httpStatusCode, message)
}

data class IntegratorErrorResponse(val status: Int, val code: String, val causes: List<String>?)