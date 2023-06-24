package com.example.savenotes.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.savenotes.model.Note

@Dao
interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // onConflict = set
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("Select * from notes_table order by id ASC") // sorted by id ascending
    fun getAllNotes(): LiveData<List<Note>>

}