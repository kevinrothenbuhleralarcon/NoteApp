package ch.kra.noteapp.notefeature.presentation.listnote

import android.util.Log
import androidx.lifecycle.*
import ch.kra.noteapp.R
import ch.kra.noteapp.core.UIEvent
import ch.kra.noteapp.notefeature.data.preferences.SettingsDataStore
import ch.kra.noteapp.notefeature.domain.model.Note
import ch.kra.noteapp.notefeature.domain.repository.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListNoteViewModel(
    private val noteRepository: NoteRepository,
    private val settingsDataStore: SettingsDataStore
): ViewModel() {

    val notes: LiveData<List<Note>> = noteRepository.getAllNotes().asLiveData()

    val isLinearLayoutSelected: LiveData<Boolean> = settingsDataStore.layoutManagerFlow.asLiveData()

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
                        message = R.string.note_deleted,
                        action = R.string.undo
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

            is ListNoteEvent.OnLayoutChanged -> {
                viewModelScope.launch {
                    settingsDataStore.saveLayoutToPreferencesStore(event.isLinearLayout)
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

class ListNoteViewModelFactory(private val noteRepository: NoteRepository, private val settingsDataStore: SettingsDataStore): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListNoteViewModel(noteRepository, settingsDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

