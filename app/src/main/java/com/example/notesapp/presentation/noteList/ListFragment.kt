package com.example.notesapp.presentation.noteList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.common.utils.extensions.getNotesDao
import com.example.notesapp.data.database.room.entityes.toNoteVO
import com.example.notesapp.databinding.FragmentListBinding
import com.example.notesapp.presentation.AddNoteFragment
import com.example.notesapp.presentation.noteList.recycleView.NotesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ListFragment : Fragment() {

    companion object {
        const val TIME_FORMAT = "dd/M/yyyy hh:mm:ss"
        const val NAVIGATION_FROM_NOTE_KEY = "NOTE_ID"
    }

    private var binding: FragmentListBinding? = null
    private var notesAdapter: NotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
        initNotesAdapter()
    }

    private fun setClickListeners() {
        binding?.fabAddNote?.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction().apply {
                    addToBackStack(null)
                        .replace(R.id.fragmentContainer, AddNoteFragment()).commit()
                }
        }
    }

    private fun initNotesAdapter() {
        notesAdapter = NotesAdapter()

        CoroutineScope(Dispatchers.IO).launch {

            notesAdapter?.listOfNotes = getNotesDao()?.getAllNotes()
                ?.sortedByDescending { noteDB ->
                    noteDB.noteLastChangedTime.toLong()
                }?.map { noteDb ->
                    val note = noteDb.toNoteVO()

                    val sdf = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
                    val currentDate = sdf.format(note.noteLastChangedTime.toLong())

                    note.noteLastChangedTime = currentDate
                    note
                }?.toMutableList()!!
        }

        notesAdapter?.onNoteClicked = { noteId ->
            val bundle = Bundle()
            bundle.putInt("NOTE_ID", noteId)

            val fragment = AddNoteFragment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit()
        }
        notesAdapter?.notesDao = getNotesDao()
        binding?.rvNotes?.adapter = notesAdapter
        binding?.rvNotes?.layoutManager = LinearLayoutManager(requireContext())
    }
}