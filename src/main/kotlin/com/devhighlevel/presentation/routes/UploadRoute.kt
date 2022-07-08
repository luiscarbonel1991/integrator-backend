package com.devhighlevel.presentation.routes

import com.devhighlevel.infraestructure.clients.AwsClient
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.uploadRoute() {

    val aws : AwsClient by inject()

    route("/upload"){
        get {
            // Test Aws S3 integration
            call.respond(mapOf("bucket" to aws.listBuckets()))
        }
    }
}