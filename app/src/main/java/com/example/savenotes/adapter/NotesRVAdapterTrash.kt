package com.example.savenotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.savenotes.R
import com.example.savenotes.model.Note
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NotesRVAdapterTrash(
    private var notesFirebase: ArrayList<Note>
): RecyclerView.Adapter<NotesRVAdapterTrash.NoteViewHolderTrash>() {

    private val noteCollectionRef = Firebase.firestore.collection("notes")

    inner class NoteViewHolderTrash(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitleTrash = itemView.findViewById<TextView>(R.id.tvTitleTrash)
        val tvTextTrash = itemView.findViewById<TextView>(R.id.tvTextTrash)
        val ivDeleteTrash = itemView.findViewById<ImageView>(R.id.ivDeleteTrash)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolderTrash {
        val viewHolder = NoteViewHolderTrash(LayoutInflater.from(parent.context).inflate(R.layout.item_note_trash, parent, false))

        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolderTrash, position: Int) {
        val currentNote = notesFirebase[position]
        holder.tvTitleTrash.text = currentNote.title
        holder.tvTextTrash.text = currentNote.text

        holder.ivDeleteTrash.setOnClickListener {
            // delete note firebase
            deleteNote(currentNote)

            if(position < notesFirebase.size) {
                // delete note recyclerview
                notesFirebase.removeAt(position)

                // update effect
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return notesFirebase.size
    }

    // delete note
    private fun deleteNote(note: Note) = CoroutineScope(Dispatchers.IO).launch {
        val noteQuery = noteCollectionRef
            .whereEqualTo("title", note.title)
            .whereEqualTo("text", note.text)
            .get()
            .await()
        if(noteQuery.documents.isNotEmpty()) {
            for(document in noteQuery) {
                noteCollectionRef.document(document.id).delete().await() // delete whole person
            }
        }
    }
}