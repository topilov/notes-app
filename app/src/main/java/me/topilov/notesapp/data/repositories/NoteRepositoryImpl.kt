package me.topilov.notesapp.data.repositories

import kotlinx.coroutines.flow.Flow
import me.topilov.notesapp.data.dao.NoteDao
import me.topilov.notesapp.data.entities.Note
import me.topilov.notesapp.domain.repositories.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
) : NoteRepository {

    override fun getAll(): Flow<List<Note>> {
        return noteDao.getAll()
    }

    override suspend fun getById(id: Int): Note {
        return noteDao.getById(id)
    }

    override suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    override suspend fun update(note: Note) {
        noteDao.update(note)
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}