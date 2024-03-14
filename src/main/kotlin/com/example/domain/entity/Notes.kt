package com.example.domain.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Notes(
    @BsonId
    val id: ObjectId,
    val note: String,
    val title: String
) {
    //todo
    //create a response here
}
