package com.devhighlevel.db.repository

import com.devhighlevel.config.MongoClient
import com.devhighlevel.db.documents.User
import org.litote.kmongo.eq

class UserRepository(
    client: MongoClient
) : GenericCrud<User>{
    private val collection = client.db().getCollection<User>("users")

    override suspend fun findAll(): List<User> = collection.find().toList()

    override suspend fun save(user: User): User {
        collection.insertOne(user)
        return user
    }
    override suspend fun update(user: User): User {
        collection.save(user)
        return user
    }

    override suspend fun delete(userId: String) {
        collection.deleteOne(User::id eq userId)
    }

    suspend fun findById(userId: String) =
        collection.findOne(User::id eq userId)

    suspend fun findByEmail(email: String) = collection.findOne(User::email eq email)
}