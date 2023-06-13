package com.example.notesapp.data.database.room.dao

import androidx.room.*
import com.example.notesapp.data.database.room.entityes.NoteDB

@Dao
interface NotesDao {

    @Insert
    fun insertNote(note: NoteDB)

    @Update
    fun updateNote(note: NoteDB)

    @Delete
    fun deleteNote(note: NoteDB)

    @Query("SELECT * FROM notes_table WHERE id = :noteId")
    fun getNote(noteId: Int): NoteDB

    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): List<NoteDB>
}