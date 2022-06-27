package com.devhighlevel.services

import com.devhighlevel.dto.UserAuthenticated
import com.devhighlevel.dto.UserLogin
import com.devhighlevel.plugins.AuthenticationException

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
}