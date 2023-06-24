package com.example.savenotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savenotes.NoteClickDeleteInterface
import com.example.savenotes.NoteClickUpdateInterface
import com.example.savenotes.NotesRVAdapter
import com.example.savenotes.R
import com.example.savenotes.model.Note
import com.example.savenotes.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NoteClickDeleteInterface, NoteClickUpdateInterface {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var viewModel: NoteViewModel
    private val noteCollectionRef = Firebase.firestore.collection("notes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set activity title
        this.title = "Notes"

        // Navigation Drawer
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // when toggle is opened and back is pressed, the navigation bar will close
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navDrawer = findViewById<NavigationView>(R.id.navDrawer)
        navDrawer.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navTrash -> Intent(this, TrashActivity::class.java).also {
                    startActivity(it)
                }
                R.id.navNotes -> Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
            }
            this.finish()
            true
        }

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvRecyler)
        val adapter = NotesRVAdapter(this, this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ViewModel
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        viewModel.allNotes.observe(this) { note ->
            adapter.updateList(note)
        }

        // Add Activity
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            Intent(this, AddActivity::class.java).also {
                startActivity(it)
            }
        }
    }


    // **** Navigation Drawer ****

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    // **** Room ****

    // Delete note
    override fun onDeleteClick(note: Note) {
        viewModel.deleteNote(note)
        insertNoteFirebase(note)
        Toast.makeText(this, "${note.title} Deleted!!", Toast.LENGTH_LONG).show()
    }

    // Update note
    override fun onNoteClick(note: Note) {
        Intent(this, UpdateActivity::class.java).also {
            it.putExtra("EXTRA_ID", note.id)
            it.putExtra("EXTRA_TITLE", note.title)
            it.putExtra("EXTRA_TEXT", note.text)
            startActivity(it)
        }
    }


    // **** Firebase ****

    // insert note
    private fun insertNoteFirebase(note: Note) = CoroutineScope(Dispatchers.IO).launch {
        try {
            noteCollectionRef.add(note).await()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}