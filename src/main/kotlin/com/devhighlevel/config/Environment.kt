package com.devhighlevel.config

object Environment{
    private val CURRENT_ENVIRONMENT : String
    private const val DEVELOPMENT = "dev"
    init {
       val env = System.getenv("KTOR_ENV") ?: System.getProperty("env")
       CURRENT_ENVIRONMENT = if(env.isNullOrEmpty()) DEVELOPMENT else env
    }

    fun getEnvironment() = CURRENT_ENVIRONMENT
}