package me.topilov.notesapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import me.topilov.notesapp.data.entities.Note
import me.topilov.notesapp.domain.repositories.NoteRepository
import me.topilov.notesapp.ui.theme.NotesAppTheme
import me.topilov.notesapp.ui.theme.Purple40
import me.topilov.notesapp.ui.theme.Purple80
import me.topilov.notesapp.ui.theme.PurpleGrey40
import me.topilov.notesapp.ui.theme.PurpleGrey80
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

@Composable
fun NotesScreen(viewModel: NoteViewModel) {
    val notes = viewModel.notes.collectAsState(initial = emptyList())

    Column {
        NotesButton(
            text = "Create note",
            onClick = {
                viewModel.insertNote(
                    Note(title = "", description = "")
                )
            }
        )
        LazyColumn {
            items(notes.value) { note ->
                NoteCard(
                    note = note,
                    onInsert = { viewModel.insertNote(note) },
                    onDelete = { viewModel.deleteNote(note) }
                )
            }
        }
    }
}

@Composable
fun NotesButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            content = { Text(text = text) },
            onClick = onClick,
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    onInsert: () -> Unit,
    onDelete: () -> Unit,
) {
    var title by remember { mutableStateOf(note.title) }
    var description by remember { mutableStateOf(note.description) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column {
                NoteTextField(
                    value = title,
                    text = "Title",
                    onValueChange = {
                        title = it
                        note.title = it
                    }
                )
                NoteTextField(
                    value = description,
                    text = "Description",
                    onValueChange = {
                        description = it
                        note.description = it
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        content = { Text(text = "Insert") },
                        onClick = onInsert,
                    )
                    Button(
                        modifier = Modifier.padding(8.dp),
                        content = { Text(text = "Delete") },
                        onClick = onDelete,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTextField(
    value: String,
    text: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        placeholder = { Text(text = text) },
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = PurpleGrey80,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
    )
}