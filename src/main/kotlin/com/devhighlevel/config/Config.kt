package com.devhighlevel.config

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

    fun loadConfig(): Config {
        val environment = Environment.getEnvironment()
        val configFile = "application.${Environment.getEnvironment()}.conf"
        println("Running in $environment environment. $configFile")
        return ConfigFactory.parseResources(configFile)
            .resolve()
    }
}