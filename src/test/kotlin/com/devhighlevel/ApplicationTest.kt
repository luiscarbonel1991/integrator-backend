package com.devhighlevel

import io.ktor.http.*
import io.ktor.client.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.devhighlevel.presentation.plugins.*

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