package com.example.notesapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // onConflict = set
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notes_table order by id ASC") // sorted by id ascending
    fun getAllNotes(): LiveData<List<Note>>

}