package ch.kra.noteapp.notefeature.presentation.addupdatenote

import androidx.lifecycle.*
import ch.kra.noteapp.core.UIEvent
import ch.kra.noteapp.notefeature.domain.model.Note
import ch.kra.noteapp.notefeature.domain.repository.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AddUpdateNoteViewModel(
    private val noteRepository: NoteRepository
): ViewModel() {

    private var noteId: Int? = null

    private val _isNewNote = MutableLiveData(true)
    val isNewNote: LiveData<Boolean> get() = _isNewNote

    private val _noteTitle = MutableLiveData("")
    val noteTitle: LiveData<String> get() = _noteTitle

    private val _noteContent = MutableLiveData("")
    val noteContent: LiveData<String> get() = _noteContent

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow().asLiveData()

    fun loadNote(id: Int) {
        if ( id >= 0) {
            viewModelScope.launch {
                val note = noteRepository.getNoteById(id)
                note?.let {
                    _noteTitle.postValue(it.title)
                    _noteContent.postValue(it.content)
                    noteId = it.id
                    _isNewNote.postValue(false)
                }
            }
        }
    }

    fun onEvent(event: AddUpdateNoteEvent) {
        when (event) {
            is AddUpdateNoteEvent.OnContentChanged -> {
                _noteContent.postValue(event.content)
            }
            is AddUpdateNoteEvent.OnTitleChanged -> {
                _noteTitle.postValue(event.title)
            }
            is AddUpdateNoteEvent.OnNavigateBackClicked -> {
                sendUIEvent(UIEvent.NavigateBack)
            }
            is AddUpdateNoteEvent.OnSaveNoteClicked -> {
                _noteTitle.value?.let {
                    if (it.isEmpty()) {
                        sendUIEvent(UIEvent.ShowSnackbar(message = "The title can't be empty"))
                        return@let
                    }
                    viewModelScope.launch {
                        noteRepository.insertNote(
                            Note(
                                title = it,
                                content = noteContent.value,
                                id = noteId
                            )
                        )
                        sendUIEvent(UIEvent.Navigate())
                    }
                }
            }
            is AddUpdateNoteEvent.OnUpdateNoteClicked -> {
                noteId?.let {
                    _noteTitle.value?.let {
                        if (it.isEmpty()) {
                            sendUIEvent(UIEvent.ShowSnackbar(message = "The title can't be empty"))
                            return
                        }
                        viewModelScope.launch {
                            noteRepository.updateNote(
                                Note(
                                    title = it,
                                    content = noteContent.value,
                                    id = noteId
                                )
                            )
                            sendUIEvent(UIEvent.Navigate())
                        }
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

    class AddUpdateNoteViewModelFactory(private val noteRepository: NoteRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddUpdateNoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddUpdateNoteViewModel(noteRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}