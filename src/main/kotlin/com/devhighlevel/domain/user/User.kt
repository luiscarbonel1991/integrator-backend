package com.devhighlevel.domain.user

import org.bson.codecs.pojo.annotations.BsonId

data class User(
    @BsonId
    val id: String?,
    var email: String,
    var password: String,
    var name: String,
    var role: String,
    var image: String? = null,
    var enabled: Boolean? = false,
    var attempts: Int? = 0
)
