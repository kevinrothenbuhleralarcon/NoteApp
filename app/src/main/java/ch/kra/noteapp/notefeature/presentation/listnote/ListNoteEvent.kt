package ch.kra.noteapp.notefeature.presentation.listnote

import ch.kra.noteapp.notefeature.domain.model.Note

sealed class ListNoteEvent {
    data class OnDeleteNoteClicked(val note: Note): ListNoteEvent()
    object UndoDelete: ListNoteEvent()
    data class OnNoteClicked(val note: Note): ListNoteEvent()
    object OnAddNoteClicked: ListNoteEvent()
}
