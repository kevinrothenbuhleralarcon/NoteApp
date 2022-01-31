package ch.kra.noteapp.notefeature.presentation.addupdatenote

import android.util.Log
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

    val noteTitle = MutableLiveData("")

    val noteContent = MutableLiveData("")

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadNote(id: Int) {
        if ( id >= 0) {
            viewModelScope.launch {
                val note = noteRepository.getNoteById(id)
                note?.let {
                    noteTitle.value = it.title
                    noteContent.value = it.content
                    noteId = it.id
                    _isNewNote.postValue(false)
                }
            }
        }
    }

    fun onEvent(event: AddUpdateNoteEvent) {
        when (event) {
            is AddUpdateNoteEvent.OnSaveNoteClicked -> {
                noteTitle.value?.let {
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
                        sendUIEvent(UIEvent.Navigate(
                            AddUpdateNoteFragmentDirections.actionAddUpdateNoteFragmentToListNoteFragment()
                        ))
                    }
                }
            }
            is AddUpdateNoteEvent.OnUpdateNoteClicked -> {
                noteId?.let {
                    noteTitle.value?.let {
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
                            sendUIEvent(UIEvent.Navigate(
                                AddUpdateNoteFragmentDirections.actionAddUpdateNoteFragmentToListNoteFragment()
                            ))
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