package com.devhighlevel.config

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class MongoClient {

        private var mongoClient: CoroutineClient = KMongo.createClient(CONNECTION_URL).coroutine

        companion object {
                private const val DATABASE = "xxxxxxxxx"
                // TODO: This CONNECTION_URL gets from application.conf
                private const val CONNECTION_URL = "xxxxxxxxxxxxxxxxx"

        }

        fun db() =  mongoClient.getDatabase(DATABASE)
}