package com.devhighlevel

import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.devhighlevel.plugins.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
         configureRouting()
        }
        client.get("/v1/health").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}