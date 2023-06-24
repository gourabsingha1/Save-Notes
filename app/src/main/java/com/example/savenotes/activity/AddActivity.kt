package com.example.savenotes.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.savenotes.R
import com.example.savenotes.model.Note
import com.example.savenotes.viewmodel.NoteViewModel

class AddActivity : AppCompatActivity() {

    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Set activity title
        this.title = "Add Note"

        // ViewModel
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
    }


    // **** Room ****

    // insert note
    fun submitData(view: View) {
        val etNote = findViewById<EditText>(R.id.etNote)
        val noteText = etNote.text.toString()
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val noteTitle = etTitle.text.toString()
        val note = Note(0, noteTitle, noteText)

        if (noteText.isNotEmpty() && noteTitle.isNotEmpty()) {
            viewModel.insertNote(note)
            Toast.makeText(this, "$noteTitle Inserted!!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Not inserted", Toast.LENGTH_LONG).show()
        }
        this.finish()
    }
}