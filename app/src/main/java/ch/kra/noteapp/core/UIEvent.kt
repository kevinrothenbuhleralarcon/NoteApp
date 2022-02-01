package ch.kra.noteapp.core

import androidx.navigation.NavDirections

sealed class UIEvent() {
    data class ShowSnackbar(val message: Int, val action: Int? = null): UIEvent()
    data class Navigate(val destination: NavDirections): UIEvent()
}