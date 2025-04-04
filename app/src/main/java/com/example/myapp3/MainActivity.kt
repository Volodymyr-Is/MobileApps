package com.example.myapp3

import Models.Note
import ViewModels.NoteViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                NotesApp()
            }
        }
    }
}

enum class Directions {
    NoteList,
    AddNote,
    NoteDetail
}

@Composable
fun NotesApp(viewModel: NoteViewModel = viewModel()) {
    val notesState = viewModel.notes.collectAsState()
    val notes = notesState.value
    val currentScreen = remember { mutableStateOf(Directions.NoteList) }
    val selectedNoteId = remember { mutableStateOf<Int?>(null) }
    val showFab = remember { mutableStateOf(true) }

    Scaffold(
        floatingActionButton = {
            if (showFab.value) {
                ExtendedFloatingActionButton(
                    text = { Text("Add a note") },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    onClick = { currentScreen.value = Directions.AddNote }
                )
            }
        }
    ) { padding ->
        when (currentScreen.value) {
            Directions.NoteList -> {
                showFab.value = true
                NoteListScreen(
                    notes = notes,
                    onNoteClick = { noteId ->
                        selectedNoteId.value = noteId
                        currentScreen.value = Directions.NoteDetail
                    },
                    modifier = Modifier.padding(padding)
                )
            }
            Directions.AddNote -> {
                showFab.value = false
                AddNoteScreen(
                    onSave = { title, text ->
                        if (title.isNotBlank() || text.isNotBlank()) {
                            viewModel.addNote(title, text)
                            currentScreen.value = Directions.NoteList
                        }
                    },
                    onBack = { currentScreen.value = Directions.NoteList },
                    modifier = Modifier.padding(padding)
                )
            }
            Directions.NoteDetail -> {
                showFab.value = false
                selectedNoteId.value?.let { noteId ->
                    val note = viewModel.getNoteById(noteId)
                    note?.let {
                        NoteDetailScreen(
                            note = it,
                            onBack = {
                                currentScreen.value = Directions.NoteList
                                selectedNoteId.value = null
                            },
                            modifier = Modifier.padding(padding)
                        )
                    } ?: run {
                        Text(
                            text = "Note not found. Please try again.",
                            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                } ?: run {
                    Text(
                        text = "No note selected. Please try again.",
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}

@Composable
fun NoteListScreen(
    notes: List<Note>,
    onNoteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (notes.isEmpty()) {
            Text(
                text = "No notes yet",
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                style = MaterialTheme.typography.headlineSmall
            )
        } else {
            LazyColumn {
                items(notes) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onNoteClick(note.id) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = note.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = note.text,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddNoteScreen(
    onSave: (String, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = remember { mutableStateOf("") }
    val text = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = text.value,
            onValueChange = { text.value = it },
            label = { Text("Text") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSave(title.value, text.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
fun NoteDetailScreen(
    note: Note,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = note.title,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = note.text,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}