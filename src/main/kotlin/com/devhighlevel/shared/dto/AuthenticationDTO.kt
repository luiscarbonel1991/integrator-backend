package com.devhighlevel.shared.dto

data class UserLogin(val username: String, val password: String)
data class UserAuthenticated(val username: String, val role: String)