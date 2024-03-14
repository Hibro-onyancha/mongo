package com.example.domain.ports

import com.example.domain.entity.Notes
import org.bson.BsonValue
import org.bson.types.ObjectId


/**let's describe how we want our database to insert,update,get and delete our notes*/
interface NotesRepository {
    /*let's find a note in our server*/
    suspend fun findNote(id: ObjectId): Notes?/*in case it does not find any note with that id should return a null*/

    /*delete*/
    suspend fun deleteNote(id: ObjectId): Long /*returns a status*/
    /*update*/
    /** here we find the specific note that we want to update using its id
    and then pass in a new parameter of the Note with the changes we want to make */
    suspend fun updateNote(id: ObjectId, notes: Notes): Long

    /*insert a note*/
    suspend fun insertNote(notes: Notes): BsonValue?

}