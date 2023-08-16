package me.topilov.notesapp.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.topilov.notesapp.data.entities.Note
import me.topilov.notesapp.domain.repositories.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    val notes = noteRepository.getAll()

    fun insertNote(note: Note = Note()) = viewModelScope.launch {
        noteRepository.insert(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        noteRepository.update(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        noteRepository.delete(note)
    }
}