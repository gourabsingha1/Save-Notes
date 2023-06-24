package com.example.savenotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude

@Entity("notes_table")
class Note (

    @PrimaryKey(true)
    var id: Int = 0,
    @ColumnInfo(name = "title")
    val title :String = "",
    @ColumnInfo(name = "text")
    val text :String = ""
)