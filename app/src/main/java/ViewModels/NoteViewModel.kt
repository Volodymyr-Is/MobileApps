package ViewModels

import Models.Note
import Models.NoteRepository
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class NoteViewModel : ViewModel() {
    private val repository = NoteRepository()
    val notes: StateFlow<List<Note>> = repository.getNotes()

    fun addNote(title: String, text: String) {
        val newNote = Note(id = (notes.value.size + 1), title = title, text = text)
        repository.addNote(newNote)
    }

    fun getNoteById(id: Int): Note? {
        return repository.getNoteById(id)
    }
}