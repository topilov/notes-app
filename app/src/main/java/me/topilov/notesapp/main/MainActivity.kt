package me.topilov.notesapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.topilov.notesapp.domain.repositories.NoteRepository
import me.topilov.notesapp.ui.notes.NotesScreen
import me.topilov.notesapp.ui.theme.NotesAppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val noteViewModel: NoteViewModel by viewModels()

    @Inject
    lateinit var noteRepository: NoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                NotesScreen(viewModel = noteViewModel)
            }
        }
    }
}