package com.example.application.respond


/*this is a data class that will be responsible for describing what the server should respond with*/
data class RespondNote(
    val id: String,
    val note: String,
    val title: String
)

