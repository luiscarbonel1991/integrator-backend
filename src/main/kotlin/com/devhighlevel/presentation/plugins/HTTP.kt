package com.devhighlevel.presentation.plugins

import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import kotlinx.coroutines.runBlocking

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(DoubleReceive)

    install(CallLogging) {
        filter { call ->
            call.request.path().startsWith("/v1")
        }
        format { call ->
            runBlocking {
                val status = call.response.status()
                val httpMethod = call.request.httpMethod.value
                val userAgent = call.request.headers["User-Agent"]
                val url = call.request.uri
                "Trace Response: Status: $status, HTTP method: $httpMethod, URL: $url, User agent: $userAgent"
            }
        }
    }
}
