package me.topilov.notesapp.ui.notes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.topilov.notesapp.R
import me.topilov.notesapp.data.entities.Note
import me.topilov.notesapp.main.NoteViewModel
import me.topilov.notesapp.ui.theme.BackgroundColor
import me.topilov.notesapp.ui.theme.PrimaryColor
import me.topilov.notesapp.ui.theme.SecondaryColor

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    notes: List<Note>,
    onCreate: () -> Unit,
    onUpdate: (Note) -> Unit,
    onDelete: (Note) -> Unit,
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor,
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = PrimaryColor,
                    onClick = onCreate
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.create_note))
                }
            },
            content = { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
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
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    onUpdate: (Note) -> Unit,
    onDelete: (Note) -> Unit,
) {
    val noteState = remember { mutableStateOf(note) }
    val title by remember { derivedStateOf { noteState.value.title } }
    val description by remember { derivedStateOf { noteState.value.description } }

    val onUpdateCallback = rememberUpdatedState(onUpdate)
    val onDeleteCallback = rememberUpdatedState(onDelete)

    LaunchedEffect(note) {
        noteState.value = note
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(),
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
                        noteState.value = noteState.value.copy(title = it)
                    }
                )
                NoteTextField(
                    value = description,
                    text = stringResource(id = R.string.description),
                    onValueChange = {
                        noteState.value = noteState.value.copy(description = it)
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        content = { Text(text = stringResource(id = R.string.update_note)) },
                        onClick = { onUpdateCallback.value(noteState.value) },
                    )
                    Button(
                        modifier = Modifier.padding(8.dp),
                        content = { Text(text = stringResource(R.string.delete_note)) },
                        onClick = { onDeleteCallback.value(noteState.value) },
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
            containerColor = Color.White,
            disabledTextColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
    )
}
