package com.devhighlevel.infraestructure.clients

import com.devhighlevel.application.exceptions.AwsException
import com.devhighlevel.application.config.Config
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.net.URI


class AwsClient(
    private val config: Config,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AwsClient::class.java)
        const val BUCKET = "integrator"
    }

    private var s3Client: S3Client = buildS3Client()

    private fun buildS3Client() = when (config.isDevEnvironment()) {
            true -> S3Client.builder()
                .endpointOverride(URI("http://localhost:4566"))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar")))
                .region(Region.US_EAST_1)
                .build()
            else -> S3Client.builder()
                .credentialsProvider(
                    StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                            config.prop("aws.accessKey"),
                            config.prop("aws.secretAccessKey")
                        )
                    )
                )
                .region(config.prop("aws.region"))
                .build()
        }

    init {
        createBucket(s3Client)
    }

    // Create a bucket by using a S3Waiter object
    private fun createBucket(s3Client: S3Client, bucketName: String = BUCKET) = runBlocking {
        try {
            when (listBuckets().find { it.name() == bucketName }) {
                null -> {
                    val s3Waiter = s3Client.waiter()
                    val bucketRequest = CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build()
                    s3Client.createBucket(bucketRequest)
                    val bucketRequestWait = HeadBucketRequest.builder()
                        .bucket(bucketName)
                        .build()
                    // Wait until the bucket is created and print out the response
                    val waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait)
                    waiterResponse.matched().response().ifPresent { println(it) }
                    logger.info("$bucketName is ready")
                }
                else -> logger.info("$bucketName already exist")
            }
        } catch (e: S3Exception) {
            throw AwsException(e.awsErrorDetails().errorMessage())
        }
    }

    suspend fun listBuckets(): MutableList<Bucket> = coroutineScope {
        withContext(dispatcher) {
            val listBucketsRequest = ListBucketsRequest.builder().build()
            val listBucketsResponse: ListBucketsResponse = s3Client.listBuckets(listBucketsRequest)
            listBucketsResponse.buckets()
        }
    }

    fun deleteBucketObjects(bucketName: String? = BUCKET, objectName: String?) {
        val toDelete = ArrayList<ObjectIdentifier>()
        toDelete.add(ObjectIdentifier.builder().key(objectName).build())
        try {
            val dor = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder().objects(toDelete).build())
                .build()
            s3Client.deleteObjects(dor)
        } catch (e: S3Exception) {
            throw AwsException(e.awsErrorDetails().errorMessage())
        }
        logger.info("Objects deleted: Bucket: $bucketName, ObjectName: $objectName. Done!")
    }
}