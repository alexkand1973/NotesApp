package com.example.notesapp.common.app

import android.app.Application
import com.example.notesapp.data.database.room.dao.NotesDao
import com.example.notesapp.data.database.room.database.AppDatabase

class App : Application() {

    var database: AppDatabase? = null
    var notesDao: NotesDao? = null

    override fun onCreate() {
        database = AppDatabase.getDatabase(this)
        notesDao = database?.getNotesDao()

        super.onCreate()
    }
}