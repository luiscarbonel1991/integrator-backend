package com.devhighlevel.routes

import com.devhighlevel.dto.UserLogin
import com.devhighlevel.services.AuthenticationService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.authenticationRoute(secret: String, issuer: String, audience: String, myRealm: String) {

   val authenticationService: AuthenticationService by inject()

    post ("/login"){
        val user = call.receive<UserLogin>()
        val userAuthenticated = authenticationService.login(user)

        // This will be moved to AuthenticationService
        val token = authenticationService.createToken(issuer, audience, secret, userAuthenticated)
        call.respond(hashMapOf("token" to token))
    }
}