package com.example.notesapp.data.database.room.entityes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp.presentation.noteList.recycleView.NoteVO

@Entity(tableName = "notes_table")
data class NoteDB(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "note_title") val noteTitle: String,
    @ColumnInfo(name = "note_description") val noteDescription: String,
    @ColumnInfo(name = "note_last_changed_time") val noteLastChangedTime: String
)

//Mapping
fun NoteDB.toNoteVO(): NoteVO {
    return NoteVO(id = id, noteTitle = noteTitle, noteLastChangedTime = noteLastChangedTime)
}