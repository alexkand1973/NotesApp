package com.example.notesapp.presentation.noteList.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.database.room.dao.NotesDao
import com.example.notesapp.data.database.room.entityes.NoteDB
import com.example.notesapp.databinding.NotesAdapterViewHolderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    var listOfNotes: MutableList<NoteVO> = mutableListOf()
    var notesDao: NotesDao? = null
    var onNoteClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            NotesAdapterViewHolderBinding
                .inflate(LayoutInflater.from(parent.context), parent, false), notesDao,
            onNoteClicked
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = listOfNotes[position]

        holder.bind(note)

        CoroutineScope(Dispatchers.IO).launch {
            notesDao?.getNote(note.id)?.let { noteDB ->
                withContext(Dispatchers.Main) {
                    holder.setOnClickListeners(noteDB) {
                        deleteNoteFromList(note)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfNotes.size
    }

    private fun deleteNoteFromList(noteVo: NoteVO) {
        val indexToDelete = listOfNotes.indexOfFirst { currentNote -> currentNote.id == noteVo.id }
        if (indexToDelete != -1) {
            listOfNotes.removeAt(indexToDelete)
            notifyItemRemoved(indexToDelete)
        }
    }

    class NotesViewHolder(
        private val binding: NotesAdapterViewHolderBinding,
        private val notesDao: NotesDao?,
        private val onNoteClicked: ((Int) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(noteVO: NoteVO) {
            binding.tvNoteTitle.text = noteVO.noteTitle
            binding.tvNoteLastChangeTime.text = noteVO.noteLastChangedTime
        }

        fun setOnClickListeners(
            currentNote: NoteDB,
            onDeleteNoteClicked: () -> Unit
        ) {
            binding.root.setOnClickListener {
                onNoteClicked?.let { onNoteClicked -> onNoteClicked(currentNote.id) }
            }
            binding.ivDeleteNote.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    notesDao?.deleteNote(currentNote)
                }
                onDeleteNoteClicked()
            }
        }
    }
}