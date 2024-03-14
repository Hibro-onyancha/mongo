package com.example.application.routes

import com.example.application.request.RequestNote
import com.example.application.request.toDomain
import com.example.domain.ports.NotesRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject

fun Route.notesRoutes() {

    val repository by inject<NotesRepository>()
    route("/note_collection") {
        post {
            val notes = call.receive<RequestNote>()
            val insertedId = repository.insertNote(notes.toDomain())
            call.respond(HttpStatusCode.Created, "Created note with id $insertedId")
        }

        delete("/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                text = "Missing note id",
                status = HttpStatusCode.BadRequest
            )
            val delete: Long = repository.deleteNote(ObjectId(id))
            if (delete == 1L) {
                return@delete call.respondText("note Deleted successfully", status = HttpStatusCode.OK)
            }
            return@delete call.respondText("note not found", status = HttpStatusCode.NotFound)
        }

        get("/{id?}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                return@get call.respondText(
                    text = "Missing id",
                    status = HttpStatusCode.BadRequest
                )
            }
            repository.findNote(ObjectId(id))?.let {
                call.respond(it)
            } ?: call.respondText("No records found for id $id")
        }

        patch("/{id?}") {
            val id = call.parameters["id"] ?: return@patch call.respondText(
                text = "Missing note id",
                status = HttpStatusCode.BadRequest
            )
            val updated = repository.updateNote(ObjectId(id), call.receive())
            call.respondText(
                text = if (updated == 1L) "note updated successfully" else "note not found",
                status = if (updated == 1L) HttpStatusCode.OK else HttpStatusCode.NotFound
            )
        }
    }
}
