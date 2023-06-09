package com.example.notesapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes_table")
class Note(

    @ColumnInfo("text") val text: String

) {

    @PrimaryKey(true) var id = 0
}