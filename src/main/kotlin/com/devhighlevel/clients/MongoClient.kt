package com.devhighlevel.clients

import com.devhighlevel.config.Config
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class MongoClient(val config: Config) {

        private val connectionUrl: String = config.prop("db.mongo.url")
        private var mongoClient: CoroutineClient = KMongo.createClient(connectionUrl).coroutine


        fun db() =  mongoClient.getDatabase(config.prop("db.mongo.database"))
}