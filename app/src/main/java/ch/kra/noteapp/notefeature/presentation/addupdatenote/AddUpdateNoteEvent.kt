package ch.kra.noteapp.notefeature.presentation.addupdatenote

sealed class AddUpdateNoteEvent {
    object OnSaveNoteClicked: AddUpdateNoteEvent()
    object OnUpdateNoteClicked: AddUpdateNoteEvent()
}
