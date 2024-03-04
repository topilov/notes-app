package me.topilov.notesapp.ui.notes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import me.topilov.notesapp.ui.theme.Typography

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
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.create_note)
                    )
                }
            },
            content = { innerPadding ->
                NotesColumn(
                    innerPadding = innerPadding,
                    notes = notes,
                    onUpdate = onUpdate,
                    onDelete = onDelete
                )
            }
        )
    }
}

@Composable
fun NotesColumn(
    innerPadding: PaddingValues,
    notes: List<Note>,
    onUpdate: (Note) -> Unit,
    onDelete: (Note) -> Unit,
) {
    var sortingOption by rememberSaveable { mutableStateOf(SortingOption.TITLE_ASCENDING) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.padding(innerPadding)) {
        SearchBar(searchQuery) { searchQuery = it }
        SortingOptionPicker(sortingOption, onSortingOptionChanged = { sortingOption = it })
        NotesList(notes, searchQuery, sortingOption, onUpdate, onDelete)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_notes),
                tint = Color.Gray.copy(alpha = 0.6f),
            )
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_notes),
                color = Color.Gray.copy(alpha = 0.6f),
            )
        },
        label = { Text(text = stringResource(id = R.string.search_notes)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            containerColor = Color.White,
        )
    )
}

@Composable
fun SortingOptionPicker(
    sortingOption: SortingOption,
    onSortingOptionChanged: (SortingOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${stringResource(id = R.string.sort)}: ${
                sortingOptionStringResource(sortingOption = sortingOption)
            }",
            modifier = Modifier
                .clickable { expanded = true }
                .padding(16.dp),
            fontSize = Typography.titleMedium.fontSize,
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            SortingOption.values().forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onSortingOptionChanged(option)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = sortingOptionStringResource(sortingOption = option)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NotesList(
    notes: List<Note>,
    searchQuery: String,
    sortingOption: SortingOption,
    onUpdate: (Note) -> Unit,
    onDelete: (Note) -> Unit,
) {
    val filteredNotes = remember(notes, searchQuery) {
        notes.filter { note ->
            note.title.contains(searchQuery, ignoreCase = true) ||
                    note.description.contains(searchQuery, ignoreCase = true)
        }
    }

    val sortedNotes = remember(filteredNotes, sortingOption) {
        when (sortingOption) {
            SortingOption.TITLE_ASCENDING -> filteredNotes.sortedBy(Note::title)
            SortingOption.TITLE_DESCENDING -> filteredNotes.sortedByDescending(Note::title)
            SortingOption.DATE_ASCENDING -> filteredNotes.sortedBy(Note::timestamp)
            SortingOption.DATE_DESCENDING -> filteredNotes.sortedByDescending(Note::timestamp)
        }
    }

    if (sortedNotes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(id = R.string.notes_empty))
        }
        return
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(sortedNotes) { note ->
            NoteCard(
                note = note,
                onUpdate = onUpdate,
                onDelete = onDelete,
            )
        }
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
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
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

@Composable
fun sortingOptionStringResource(sortingOption: SortingOption): String {
    return stringResource(
        id = when (sortingOption) {
            SortingOption.TITLE_ASCENDING -> R.string.sort_by_title_ascending
            SortingOption.TITLE_DESCENDING -> R.string.sort_by_title_descending
            SortingOption.DATE_ASCENDING -> R.string.sort_by_date_ascending
            SortingOption.DATE_DESCENDING -> R.string.sort_by_title_descending
        }
    )
}
