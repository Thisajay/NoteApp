package com.example.notesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapp.Note
import com.example.notesapp.viewmodel.NoteViewModel
import com.example.notesapp.databinding.FragmentEditNotesBinding

class EditNotesFragment : Fragment() {

    private var _binding: FragmentEditNotesBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val note = arguments?.getParcelable<Note>("note")
        val title = if (note == null) "Add New Note" else "Edit Note"
        requireActivity().title = title

        note?.let {
            binding.titleEditText.setText(it.title)
            binding.contentEditText.setText(it.content)
            binding.categoryEditText.setText(it.category)
        }

        noteViewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (message == "Note created successfully" || message == "Note updated successfully") {
                findNavController().navigateUp()
            }
        })

        binding.saveButton.setOnClickListener {
            val updatedTitle = binding.titleEditText.text.toString().trim()
            val updatedContent = binding.contentEditText.text.toString().trim()
            val updatedCategory = binding.categoryEditText.text.toString().trim()
            if (updatedTitle.isEmpty() || updatedContent.isEmpty() || updatedCategory.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newNote = Note(
                id = note?.id ?: 0,
                title = updatedTitle,
                content = updatedContent,
                category = updatedCategory
            )

            if (note == null) {
                noteViewModel.insert(newNote)
            } else {
                noteViewModel.update(newNote)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//package com.example.notesapp
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import com.example.notesapp.databinding.FragmentEditNotesBinding
//
//class EditNotesFragment : Fragment() {
//
//    private var _binding: FragmentEditNotesBinding? = null
//    private val binding get() = _binding!!
//
//    private val noteViewModel: NoteViewModel by viewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentEditNotesBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val note = arguments?.getParcelable<Note>("note")
//        val title = if (note == null) "Add New Note" else "Edit Note"
//        requireActivity().title = title
//
//        note?.let {
//            binding.titleEditText.setText(it.title)
//            binding.contentEditText.setText(it.content)
//            binding.categoryEditText.setText(it.category)
//        }
//        binding.saveButton.setOnClickListener {
//            val updatedTitle = binding.titleEditText.text.toString().trim()
//            val updatedContent = binding.contentEditText.text.toString().trim()
//            val updatedCategory = binding.categoryEditText.text.toString().trim()
//            if (updatedTitle.isEmpty() || updatedContent.isEmpty() || updatedCategory.isEmpty()) {
//                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
//                    .show()
//                return@setOnClickListener
//            }
//
//            val newNote = Note(
//                id = note?.id ?: 0,
//                title = updatedTitle,
//                content = updatedContent,
//                category = updatedCategory,
//            )
//
//            if (note == null) {
//                noteViewModel.insert(newNote)
//            } else {
//                noteViewModel.update(newNote)
//            }
//
//            findNavController().navigateUp()
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
