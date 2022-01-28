package ch.kra.noteapp.notefeature.presentation.addupdatenote

sealed class AddUpdateNoteEvent {
    object OnSaveNoteClicked: AddUpdateNoteEvent()
    object OnNavigateBackClicked: AddUpdateNoteEvent()
    object OnUpdateNoteClicked: AddUpdateNoteEvent()
    data class OnTitleChanged(val title: String): AddUpdateNoteEvent()
    data class OnContentChanged(val content: String): AddUpdateNoteEvent()
}
