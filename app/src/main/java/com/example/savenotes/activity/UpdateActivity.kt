package com.example.savenotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.savenotes.R
import com.example.savenotes.model.Note
import com.example.savenotes.viewmodel.NoteViewModel
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UpdateActivity : AppCompatActivity() {

    lateinit var viewModel: NoteViewModel
    private val noteCollectionRef = Firebase.firestore.collection("notes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Set activity title
        this.title = "Update Note"

        // ViewModel
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        val etTitleUpdate = findViewById<EditText>(R.id.etTitleUpdate)
        val etTextUpdate = findViewById<EditText>(R.id.etTextUpdate)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

//        on below line we are getting data passed via an intent.
        val oldId = intent.getIntExtra("EXTRA_ID", -1)
        val oldTitle = intent.getStringExtra("EXTRA_TITLE")
        val oldText = intent.getStringExtra("EXTRA_TEXT")

        etTitleUpdate.setText(oldTitle)
        etTextUpdate.setText(oldText)

        btnUpdate.setOnClickListener {
            val newTitle = etTitleUpdate.text.toString()
            val newText = etTextUpdate.text.toString()
            val newNote = Note(0, newTitle, newText)
            if (newTitle.isNotEmpty() && newText.isNotEmpty()) {
                newNote.id = oldId
                viewModel.updateNote(newNote)
                Toast.makeText(this, "$oldTitle Updated!!", Toast.LENGTH_LONG).show()
            }

            // opening the new activity on below line
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}