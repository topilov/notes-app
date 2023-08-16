package me.topilov.notesapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import me.topilov.notesapp.data.entities.Note

interface NoteRepository {

    fun getAll(): Flow<List<Note>>

    suspend fun getById(id: Int): Note

    suspend fun insert(note: Note)

    suspend fun update(note: Note)

    suspend fun delete(note: Note)
}