package com.devhighlevel.plugins

import com.devhighlevel.routes.authenticationRoute
import com.devhighlevel.routes.usersRoute
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

fun Application.configureRouting() {


    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    routing {
        trace { application.log.trace(it.buildText()) }
        v1(secret, issuer, audience, myRealm)
    }

    statusPages()

}

private fun Routing.v1(
    secret: String,
    issuer: String,
    audience: String,
    myRealm: String
) {
    route("/v1") {
        authenticationRoute(secret, issuer, audience, myRealm)

        authenticate("auth-jwt") {
            usersRoute()

            get("/") {
                call.respondText("Health OK!")
            }

        }
    }
}

class AuthenticationException(message: String) : RuntimeException(message)
class AuthorizationException(message: String) : RuntimeException(message)
