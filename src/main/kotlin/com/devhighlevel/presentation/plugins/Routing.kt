package com.devhighlevel.presentation.plugins

import com.devhighlevel.presentation.routes.authenticationRoute
import com.devhighlevel.presentation.routes.uploadRoute
import com.devhighlevel.presentation.routes.usersRoute
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    routing {
        trace()
        route("/v1") {

            uploadRoute()
            authenticationRoute(secret, issuer, audience, myRealm)

            authenticate("auth-jwt") {
                get("/health") {
                    call.respondText("Health OK!")
                }
                usersRoute()
            }
        }
    }

    statusPages()

}

private fun Routing.trace() {
    trace {
        application.log.info(
            "Trace Request: HTTP method: ${it.call.request.httpMethod.value}, " +
                    "URL: ${it.call.request.uri}, " +
                    "User agent: ${it.call.request.userAgent()}"
        )
    }
}
