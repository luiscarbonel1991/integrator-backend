package com.devhighlevel

import io.ktor.server.application.*
import com.devhighlevel.plugins.*

fun main(args: Array<String>) {
    ModuleLoader.init()
    io.ktor.server.netty.EngineMain.main(args)
}


@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.v1Module() {
    configureRouting()
    configureSecurity()
    configureHTTP()
    configureSerialization()
}
