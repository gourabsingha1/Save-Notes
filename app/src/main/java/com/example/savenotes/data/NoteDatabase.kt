package com.example.savenotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.savenotes.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
public abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDAO(): NoteDAO

    // Singleton prevents multiple instances of database opening at the same time
    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                // if the INSTANCE is not null, then return it,
                // else create the database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}