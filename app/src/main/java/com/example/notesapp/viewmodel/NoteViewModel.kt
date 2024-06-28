package com.example.notesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Note
import com.example.notesapp.NoteDatabase
import com.example.notesapp.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }

    fun insert(note: Note) = viewModelScope.launch {
        if (repository.isTitleExists(note.title)) {
            _toastMessage.postValue("Title already exists")
        } else {
            repository.insert(note)
            _toastMessage.postValue("Note created successfully")
        }
    }

    fun update(note: Note) = viewModelScope.launch {
        val existingNote = repository.getNoteById(note.id)
        if (existingNote?.title != note.title && repository.isTitleExists(note.title)) {
            _toastMessage.postValue("Title already exists")
        } else {
            repository.update(note)
            _toastMessage.postValue("Note updated successfully")
        }
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
        _toastMessage.postValue("Note deleted successfully")
    }
}


//package com.example.notesapp
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//
//class NoteViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository: NoteRepository
//    val allNotes: LiveData<List<Note>>
//
//    init {
//        val noteDao = NoteDatabase.getDatabase(application).noteDao()
//        repository = NoteRepository(noteDao)
//        allNotes = repository.allNotes
//    }
//
//    fun insert(note: Note) = viewModelScope.launch {
//        repository.insert(note)
//    }
//
////    fun update(note: Note) = viewModelScope.launch {
////        repository.update(note)
////    }
//
//
//    fun delete(note: Note) = viewModelScope.launch {
//        repository.delete(note)
//    }
//
//}
