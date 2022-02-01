package ch.kra.noteapp.notefeature.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import ch.kra.noteapp.notefeature.data.local.NoteDatabase
import ch.kra.noteapp.notefeature.data.preferences.SettingsDataStore
import ch.kra.noteapp.notefeature.data.repository.NoteRepositoryImpl
import ch.kra.noteapp.notefeature.domain.repository.NoteRepository

object ServiceLocator {

    private var noteDatabase: NoteDatabase? = null

    @Volatile
    private var settingsDataStore: SettingsDataStore? = null

    @Volatile
    var noteRepository: NoteRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): NoteRepository {
        synchronized(this) {
            return noteRepository ?: createNoteRepository(context)
        }
    }

    private fun createNoteRepository(context: Context): NoteRepository {
        val database = noteDatabase ?: createNoteDatabase(context)
        val newRepository = NoteRepositoryImpl(
            database.noteDao
        )
        noteRepository = newRepository
        return newRepository
    }

    private fun createNoteDatabase(context: Context): NoteDatabase {
        val newDatabase = Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "noteDatabase"
        ).build()
        noteDatabase = newDatabase
        return newDatabase
    }

    fun provideDataStore(context: Context): SettingsDataStore {
        return settingsDataStore ?: synchronized(this) {
            val dataStore = SettingsDataStore(context)
            settingsDataStore = dataStore
            dataStore
        }
    }
}