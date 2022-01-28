package ch.kra.noteapp.core

sealed class UIEvent() {
    data class ShowSnackbar(val message: String, val action: String? = null): UIEvent()
    object NavigateBack: UIEvent()
    data class Navigate(val noteId: Int? = null): UIEvent()
}