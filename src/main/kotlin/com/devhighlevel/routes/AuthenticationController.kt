package com.devhighlevel.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.devhighlevel.dto.UserLogin
import com.devhighlevel.services.AuthenticationService
import com.devhighlevel.services.UserService
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject
import java.time.ZoneId
import java.util.*
import kotlin.math.pow


fun Route.authenticationRoute(secret: String, issuer: String, audience: String, myRealm: String) {

   val authenticationService: AuthenticationService by inject()

    post ("/login"){
        val user = call.receive<UserLogin>()
        val userAuthenticated = authenticationService.login(user)

        // This will be moved to AuthenticationService
        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", userAuthenticated.username)
            .withClaim("role", userAuthenticated.role)
            .withExpiresAt( Date(System.currentTimeMillis() + 6 * 3600000 ) )
            .sign(Algorithm.HMAC256(secret))
        call.respond(hashMapOf("token" to token))
    }
}