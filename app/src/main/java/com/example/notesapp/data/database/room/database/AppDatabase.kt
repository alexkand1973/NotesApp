package com.example.notesapp.data.database.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.data.database.room.dao.NotesDao
import com.example.notesapp.data.database.room.entityes.NoteDB

@Database(entities = [NoteDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNotesDao(): NotesDao

    companion object {

        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "notes_table" //notes_database
                ).build() //.allowMainThreadQueries().build()
                INSTANCE = instance

                instance
            }
        }
    }
}