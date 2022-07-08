package com.devhighlevel.application.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory


class Config {

    private val config: Config

    init {
        config = loadConfig()
    }

    @Suppress("UNCHECKED_CAST")
    fun<T> prop(prop: String): T = config.getAnyRef(prop) as T

    fun getConfig() = config

    private fun loadConfig(): Config {
        val environment = Environment.currentEnvironment()
        val configFile = "application.${Environment.currentEnvironment().envName}.conf"
        println("Running in $environment environment. $configFile")
        return ConfigFactory.parseResources(configFile)
            .resolve()
    }

    fun isDevEnvironment() = Environment.isDevEnvironment()
}