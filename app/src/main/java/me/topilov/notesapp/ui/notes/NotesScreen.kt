package me.topilov.notesapp.ui.notes

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.topilov.notesapp.R
import me.topilov.notesapp.data.entities.Note
import me.topilov.notesapp.main.NoteViewModel
import me.topilov.notesapp.ui.theme.PurpleGrey80

@Composable
fun NotesScreen(viewModel: NoteViewModel) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    NotesScreen(
        notes = notes,
        onCreate = viewModel::insertNote,
        onUpdate = viewModel::updateNote,
        onDelete = viewModel::deleteNote,
    )
}

@Composable
fun NotesScreen(
    notes: List<Note>,
    onCreate: () -> Unit,
    onUpdate: (Note) -> Unit,
    onDelete: (Note) -> Unit,
) {
    Column {
        NotesButton(
            text = stringResource(id = R.string.create_note),
            onClick = onCreate,
        )
        LazyColumn {
            items(notes) { note ->
                NoteCard(
                    note = note,
                    onUpdate = onUpdate,
                    onDelete = onDelete,
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
    onUpdate: (Note) -> Unit,
    onDelete: (Note) -> Unit,
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
                    text = stringResource(id = R.string.title),
                    onValueChange = {
                        title = it
                        note.title = it
                    }
                )
                NoteTextField(
                    value = description,
                    text = stringResource(id = R.string.description),
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
                        content = { Text(text = stringResource(id = R.string.update_note)) },
                        onClick = { onUpdate(note) },
                    )
                    Button(
                        modifier = Modifier.padding(8.dp),
                        content = { Text(text = stringResource(R.string.delete_note)) },
                        onClick = { onDelete(note) },
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