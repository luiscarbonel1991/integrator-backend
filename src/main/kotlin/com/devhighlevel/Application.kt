package com.devhighlevel

import com.devhighlevel.config.Config
import com.devhighlevel.config.ModuleLoader
import com.devhighlevel.plugins.configureHTTP
import com.devhighlevel.plugins.configureRouting
import com.devhighlevel.plugins.configureSecurity
import com.devhighlevel.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    ModuleLoader.init()

    //io.ktor.server.netty.EngineMain.main(args)
    val env = getApplicationEngineEnvironment()
    embeddedServer(Netty, env).start(wait = true)
}

private fun getApplicationEngineEnvironment(): ApplicationEngineEnvironment {
    val configLoaded = Config().getConfig()
    return applicationEngineEnvironment {
        this.config = HoconApplicationConfig(configLoaded)
        connector {
            this.host = configLoaded.getAnyRef("ktor.deployment.host") as String
            this.port = configLoaded.getAnyRef("ktor.deployment.port") as Int
        }
    }
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.v1Module() {
    configureRouting()
    configureSecurity()
    configureHTTP()
    configureSerialization()
}
