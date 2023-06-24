package com.example.savenotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savenotes.R
import com.example.savenotes.model.Note
import com.example.savenotes.adapter.NotesRVAdapterTrash
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class TrashActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesList: ArrayList<Note>
    private val noteCollectionRef = Firebase.firestore.collection("notes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)

        // Set activity title
        this.title = "Trash"

        // Navigation Drawer
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayoutTrash)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // when toggle is opened and back is pressed, the navigation bar will close
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navView = findViewById<NavigationView>(R.id.navDrawer)
        navView.setNavigationItemSelectedListener {
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
        recyclerView = findViewById(R.id.rvTrash)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notesList = arrayListOf()

        retrieveNotesRealtime()
    }


    // **** Navigation Drawer ****

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    /// **** Firebase ****

    // retrieve data realtime
    private fun retrieveNotesRealtime() {
        noteCollectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            // does not let notes reappear after deleting
            notesList.clear()

            firebaseFirestoreException?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            querySnapshot?.let {
                for(document in it) {
                    // convert data from document to Note class
                    val note = document.toObject<Note>()
                    notesList.add(note!!)
                }

                // add the list to recylerView
                val adapter = NotesRVAdapterTrash(notesList)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }
}