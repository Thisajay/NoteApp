package com.example.notesapp.repository
import androidx.lifecycle.LiveData
import com.example.notesapp.Note
import com.example.notesapp.NoteDao

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
    suspend fun isTitleExists(title: String): Boolean {
        return noteDao.getNoteByTitle(title) != null
    }
    suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

}
