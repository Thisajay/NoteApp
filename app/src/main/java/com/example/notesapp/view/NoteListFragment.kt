package com.example.notesapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.databinding.FragmentNoteListBinding
import androidx.navigation.fragment.findNavController
import com.example.notesapp.Note
import com.example.notesapp.view.viewhelper.NoteAdapter
import com.example.notesapp.viewmodel.NoteViewModel
import com.example.notesapp.R

class NoteListFragment : Fragment() {

    private val noteViewModel: NoteViewModel by viewModels()
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoteAdapter
    private var notesList: List<Note> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Note List"
        setupRecyclerView()

        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            notes?.let {
                notesList = it
                adapter.submitList(it)
                toggleEmptyViewVisibility(it.isEmpty())
            }
        }


        binding.addNotes.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_editNotesFragment)
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
               filterNotes(s.toString().trim())
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = NoteAdapter(
            onEditClick = { note ->
                val bundle = Bundle().apply {
                    putParcelable("note", note)
                }
                findNavController().navigate(
                    R.id.action_noteListFragment_to_editNotesFragment,
                    bundle
                )
            },
            onDeleteClick = { note ->
                noteViewModel.delete(note)
            }
        )
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notesRecyclerView.adapter = adapter
        noteViewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun filterNotes(query: String) {
        val filteredList = notesList.filter { note ->
            note.title.contains(query, ignoreCase = true) || note.content.contains(
                query,
                ignoreCase = true
            )
        }
        adapter.submitList(filteredList)
    }

    private fun toggleEmptyViewVisibility(isEmpty: Boolean) {
        binding.emptyTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
