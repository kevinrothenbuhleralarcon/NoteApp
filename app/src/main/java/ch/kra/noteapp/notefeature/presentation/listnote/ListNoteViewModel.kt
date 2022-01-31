package ch.kra.noteapp.notefeature.presentation.listnote

import android.util.Log
import androidx.lifecycle.*
import ch.kra.noteapp.R
import ch.kra.noteapp.core.UIEvent
import ch.kra.noteapp.notefeature.domain.model.Note
import ch.kra.noteapp.notefeature.domain.repository.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListNoteViewModel(
    private val noteRepository: NoteRepository
): ViewModel() {

    val notes: LiveData<List<Note>> = noteRepository.getAllNotes().asLiveData()

    private val _isLinearLayoutSelected = MutableLiveData(true)
    val isLinearLayoutSelected: LiveData<Boolean> get() = _isLinearLayoutSelected

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var lastDeletedNote: Note? = null

    fun onEvent(event: ListNoteEvent) {
        when (event) {
            is ListNoteEvent.OnAddNoteClicked -> {
                sendUIEvent(UIEvent.Navigate(
                    destination = ListNoteFragmentDirections.actionListNoteFragmentToAddUpdateNoteFragment(-1)
                ))
            }
            is ListNoteEvent.OnDeleteNoteClicked -> {
                viewModelScope.launch {
                    lastDeletedNote = event.note
                    noteRepository.deleteNote(event.note)
                    sendUIEvent(UIEvent.ShowSnackbar(
                        message = "Note deleted",
                        action = "Undo"
                    ))
                }
            }
            is ListNoteEvent.OnNoteClicked -> {
                sendUIEvent(UIEvent.Navigate(
                    destination = ListNoteFragmentDirections.actionListNoteFragmentToAddUpdateNoteFragment(event.note.id ?: -1)
                ))
            }
            is ListNoteEvent.UndoDelete -> {
               lastDeletedNote?.let {
                   viewModelScope.launch {
                       noteRepository.insertNote(it)
                   }
               }
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}

class ListNoteViewModelFactory(private val noteRepository: NoteRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListNoteViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

