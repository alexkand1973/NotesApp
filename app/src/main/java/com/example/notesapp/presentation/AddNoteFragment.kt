package com.example.notesapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.notesapp.R
import com.example.notesapp.common.utils.extensions.getNotesDao
import com.example.notesapp.data.database.room.entityes.NoteDB
import com.example.notesapp.databinding.FragmentAddNoteBinding
import com.example.notesapp.presentation.noteList.ListFragment.Companion.NAVIGATION_FROM_NOTE_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class AddNoteFragment : Fragment() {

    companion object {
        const val MIN_SYMBOLS_NUMBER = 0
        const val DEFAULT_VALUE_FOR_NOTE_DB_ID = 0
    }

    private var binding: FragmentAddNoteBinding? = null
    private var isNoteCreated: Boolean? = false
    private var currentNote: NoteDB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments?.getInt(NAVIGATION_FROM_NOTE_KEY) != null) {
            isNoteCreated = true

            val noteDeferred = CoroutineScope(Dispatchers.IO).async {
                getNotesDao()?.getNote(arguments?.getInt(NAVIGATION_FROM_NOTE_KEY)!!)
            }
            CoroutineScope(Dispatchers.Main).launch {
                currentNote = noteDeferred.await()

                loadCurrentNoteData()
                binding?.btnSaveNote?.text =
                    getString(R.string.btn_save_note_update_note_text_add_note_fragment)
            }
        }
        setOnClickListeners()
    }

    private fun loadCurrentNoteData() {
        binding?.etNoteTitle?.setText(currentNote?.noteTitle)
        binding?.etNoteDescription?.setText(currentNote?.noteDescription)
    }

    private fun setOnClickListeners() {
        binding?.btnSaveNote?.setOnClickListener {
            if (binding?.etNoteTitle?.text?.length != MIN_SYMBOLS_NUMBER) {
                val noteTitle = binding?.etNoteTitle?.text.toString()
                val noteDescription = binding?.etNoteDescription?.text.toString()
                val currentDateInMills = System.currentTimeMillis()

                CoroutineScope(Dispatchers.IO).launch {
                    val note = NoteDB(
                        id = currentNote?.id ?: DEFAULT_VALUE_FOR_NOTE_DB_ID,
                        noteTitle = noteTitle, noteDescription = noteDescription,
                        noteLastChangedTime = currentDateInMills.toString()
                    )

                    if (isNoteCreated == true) {
                        getNotesDao()?.updateNote(note)
                    } else {
                        getNotesDao()?.insertNote(note)
                    }
                }
                requireActivity().supportFragmentManager.apply {
                    popBackStack()
                    beginTransaction().replace(
                        R.id.fragmentContainer,
                        com.example.notesapp.presentation.noteList.ListFragment()
                    ).commit()
                }
                //вторым аргументом в replace каккой ListFragment брать?
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_note_title_toast_text_add_note_fragment),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}