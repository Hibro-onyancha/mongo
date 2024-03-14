package com.example.application.request

import com.example.domain.entity.Notes
import org.bson.types.ObjectId

data class RequestNote(
    val title: String,
    val note: String
)

fun RequestNote.toDomain(): Notes {
    return Notes(
        id = ObjectId(),
        title = title,
        note = note
    )
}
