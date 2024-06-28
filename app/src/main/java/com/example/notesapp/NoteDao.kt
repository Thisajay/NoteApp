package com.example.notesapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("SELECT * FROM note_table WHERE title = :title LIMIT 1")
    suspend fun getNoteByTitle(title: String): Note?

    @Query("SELECT * FROM note_table WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Int): Note?

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

}
