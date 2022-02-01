package ch.kra.noteapp

import android.app.Application
import android.app.Service
import ch.kra.noteapp.notefeature.data.preferences.SettingsDataStore
import ch.kra.noteapp.notefeature.di.ServiceLocator
import ch.kra.noteapp.notefeature.domain.repository.NoteRepository

class NoteApplication: Application() {
    val noteRepository: NoteRepository
        get() = ServiceLocator.provideRepository(this)

    val dataStore: SettingsDataStore
        get() = ServiceLocator.provideDataStore(this)
}