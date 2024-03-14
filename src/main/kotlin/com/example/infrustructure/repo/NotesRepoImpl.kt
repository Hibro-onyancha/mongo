package com.example.infrustructure.repo

import com.example.domain.entity.Notes
import com.example.domain.ports.NotesRepository
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.BsonValue
import org.bson.types.ObjectId

class NotesRepoImpl(private val db: MongoDatabase) : NotesRepository {
    companion object {
        const val COLLECTION = "note_collection"
    }

    override suspend fun findNote(id: ObjectId): Notes? =
        db.getCollection<Notes>(COLLECTION).withDocumentClass<Notes>().find(Filters.eq("_id", id)).firstOrNull()


    override suspend fun deleteNote(id: ObjectId): Long {
        try {
            val result = db.getCollection<Notes>(COLLECTION).deleteOne(Filters.eq("_id", id))
            return result.deletedCount
        } catch (e: MongoException) {
            System.err.println("unable to delete note $e")
        }
        return 0
    }

    override suspend fun updateNote(id: ObjectId, notes: Notes): Long {
        try {
            val query = Filters.eq("_id", id)
            val updates = Updates.combine(
                Updates.set(Notes::note.name, notes.note), Updates.set(Notes::title.name, notes.title)
            )
            val updateOptions = UpdateOptions().upsert(true)
            val result = db.getCollection<Notes>(COLLECTION).updateOne(query, updates, updateOptions)
            return result.modifiedCount
        } catch (e: MongoException) {
            System.err.println("unable to update $e")
        }
        return 0
    }

    override suspend fun insertNote(notes: Notes): BsonValue? {
        try {
            val result = db.getCollection<Notes>(COLLECTION).insertOne(notes)
            return result.insertedId
        } catch (e: MongoException) {
            System.err.println("unable to insert the note $e")
        }
        return null
    }
}