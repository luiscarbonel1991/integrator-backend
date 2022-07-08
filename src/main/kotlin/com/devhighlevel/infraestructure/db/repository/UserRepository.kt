package com.devhighlevel.infraestructure.db.repository

import com.devhighlevel.infraestructure.clients.MongoClient
import com.devhighlevel.infraestructure.db.documents.User
import org.litote.kmongo.eq

class UserRepository(
    client: MongoClient
) : GenericCrud<User>{
    private val collection = client.db().getCollection<User>("users")

    override suspend fun findAll(): List<User> = collection.find().toList()

    override suspend fun save(e: User): User {
        collection.insertOne(e)
        return e
    }
    override suspend fun update(e: User): User {
        collection.save(e)
        return e
    }

    override suspend fun delete(id: String) {
        collection.deleteOne(User::id eq id)
    }

    suspend fun findById(userId: String) =
        collection.findOne(User::id eq userId)

    suspend fun findByEmail(email: String) = collection.findOne(User::email eq email)
}