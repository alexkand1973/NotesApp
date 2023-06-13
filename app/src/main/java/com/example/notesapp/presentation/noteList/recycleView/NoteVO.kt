package com.example.notesapp.presentation.noteList.recycleView

data class NoteVO(val id: Int = 0, val noteTitle: String, var noteLastChangedTime: String)

//fun NoteVO.toNoteDB(): NoteDB {
//    return NoteDB(noteTitle = noteTitle, noteLastChangedTime = noteLastChangedTime)
//}