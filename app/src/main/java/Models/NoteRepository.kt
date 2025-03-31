package Models

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteRepository {
    private val notes = mutableListOf<Note>()
    private val notesFlow = MutableStateFlow<List<Note>>(emptyList())

    fun getNotes(): StateFlow<List<Note>> = notesFlow.asStateFlow()

    fun addNote(note: Note) {
        notes.add(note)
        notesFlow.value = notes.toList()
    }

    fun getNoteById(id: Int): Note? {
        return notes.find { it.id == id }
    }
}