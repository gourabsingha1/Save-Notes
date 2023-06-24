package com.example.savenotes.repository

import androidx.lifecycle.LiveData
import com.example.savenotes.data.NoteDAO
import com.example.savenotes.model.Note

class NoteRepository(private val noteDAO: NoteDAO) {

    val allNotes: LiveData<List<Note>> = noteDAO.getAllNotes()

    suspend fun insert(note: Note) {
        noteDAO.insert(note)
    }

    suspend fun delete(note: Note) {
        noteDAO.delete(note)
    }

    suspend fun update(note: Note) {
        noteDAO.update(note)
    }

}