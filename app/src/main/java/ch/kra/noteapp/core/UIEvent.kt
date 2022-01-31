package ch.kra.noteapp.core

import androidx.navigation.NavDirections

sealed class UIEvent() {
    data class ShowSnackbar(val message: String, val action: String? = null): UIEvent()
    data class Navigate(val destination: NavDirections): UIEvent()
}