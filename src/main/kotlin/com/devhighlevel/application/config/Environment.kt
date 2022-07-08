package com.devhighlevel.application.config

import java.lang.IllegalArgumentException

object Environment{
    private val CURRENT_ENVIRONMENT : EnvironmentType
    private val DEVELOPMENT = EnvironmentType.DEV
    init {
       val env = System.getenv("KTOR_ENV") ?: System.getProperty("env")
       CURRENT_ENVIRONMENT = EnvironmentType.fromEnvName(env)
    }

    fun currentEnvironment() = CURRENT_ENVIRONMENT

    fun isDevEnvironment() = CURRENT_ENVIRONMENT == EnvironmentType.DEV
}

enum class EnvironmentType(val envName: String) {
    DEV("dev"),
    PROD("prod"),
    TEST("test");

    companion object {
        fun fromEnvName(name: String): EnvironmentType {
            val allEnvironments = values()
            return when(val env = allEnvironments.find { name == it.envName }) {
                null -> throw IllegalArgumentException("Environment $name not exist. Try with ${allEnvironments.map { it.envName }}")
                else -> env
            }
        }
    }

}