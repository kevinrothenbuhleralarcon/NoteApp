package ch.kra.noteapp

import android.app.Application
import ch.kra.noteapp.notefeature.di.ServiceLocator
import ch.kra.noteapp.notefeature.domain.repository.NoteRepository

class NoteApplication: Application() {
    val noteRepository: NoteRepository
        get() = ServiceLocator.provideRepository(this)
}