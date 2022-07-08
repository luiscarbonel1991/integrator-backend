package com.devhighlevel.application.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.devhighlevel.application.exceptions.AuthenticationException
import com.devhighlevel.shared.dto.UserAuthenticated
import com.devhighlevel.shared.dto.UserLogin
import java.util.*

class AuthenticationService(private val userService: UserService) {

    suspend fun login(userLogin: UserLogin): UserAuthenticated {
        try {
            val user = userService.findByEmail(userLogin.username)
            if (user.enabled == false) {
                throw AuthenticationException("Unauthorized. User not enabled.")
            }
            if (user.password != userLogin.password) {
                user.attempts = user.attempts?.plus(1)
                if (user.attempts!! >= 3) {
                    user.enabled = false
                    user.attempts = 3
                }
                userService.update(user)
                throw AuthenticationException("Unauthorized. Incorrect user or password")
            }
            return UserAuthenticated(user.email, user.role)
        } catch (e: Exception) {
            throw AuthenticationException("Unauthorized. User ${userLogin.username} not found")
        }
    }

     fun createToken(issuer: String, audience: String, secret: String, userAuthenticated: UserAuthenticated): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("username", userAuthenticated.username)
        .withClaim("role", userAuthenticated.role)
        .withExpiresAt( Date(System.currentTimeMillis() + 6 * 3600000 ) )
        .sign(Algorithm.HMAC256(secret))
}