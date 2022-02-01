package ch.kra.noteapp.notefeature.presentation.listnote

import android.widget.LinearLayout
import ch.kra.noteapp.notefeature.domain.model.Note

sealed class ListNoteEvent {
    data class OnDeleteNoteClicked(val note: Note): ListNoteEvent()
    object UndoDelete: ListNoteEvent()
    data class OnNoteClicked(val note: Note): ListNoteEvent()
    object OnAddNoteClicked: ListNoteEvent()
    data class OnLayoutChanged(val isLinearLayout: Boolean): ListNoteEvent()
}
