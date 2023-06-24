package com.example.savenotes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.savenotes.model.Note

class NotesRVAdapter(
    private val context: Context,
    private val noteClickDeleteInterface: NoteClickDeleteInterface,
    private val noteClickUpdateInterface: NoteClickUpdateInterface
): RecyclerView.Adapter<NotesRVAdapter.NoteViewHolder>() {

    val allNotes = ArrayList<Note>()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvText = itemView.findViewById<TextView>(R.id.tvText)
        val deleteButton = itemView.findViewById<ImageView>(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder = NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false))

        // delete
        viewHolder.deleteButton.setOnClickListener {
            noteClickDeleteInterface.onDeleteClick(allNotes[viewHolder.adapterPosition])
        }

        // update
        viewHolder.itemView.setOnClickListener {
            noteClickUpdateInterface.onNoteClick(allNotes[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = allNotes[position]
        holder.tvTitle.text = currentNote.title
        holder.tvText.text = currentNote.text
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }
}

interface NoteClickDeleteInterface {
    fun onDeleteClick(note: Note)
}

interface NoteClickUpdateInterface {
    fun onNoteClick(note: Note)
}