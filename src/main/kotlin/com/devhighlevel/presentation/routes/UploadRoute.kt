package com.devhighlevel.presentation.routes

import com.devhighlevel.infraestructure.clients.AwsClient
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Route.uploadRoute() {

    val aws : AwsClient by inject()

    route("/upload"){
        var fileDescription = ""
        var fileName = ""

        get {
            // Test Aws S3 integration
            call.respond(mapOf("bucket" to aws.listBuckets()))
        }

        // Upload multiples files
        post {
            call.receiveMultipart().forEachPart { part ->
                when(part) {
                    is PartData.FormItem -> fileDescription = part.value
                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        val fileBytes = part.streamProvider().readBytes()
                        File("../../uploads/$fileName").writeBytes(fileBytes)
                    }
                    else -> { throw BadRequestException("It is not a file.") }
                }
            }
            call.respondText("$fileDescription is uploaded to 'uploads/$fileName'")
        }
    }
}