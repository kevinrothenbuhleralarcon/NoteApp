package ch.kra.noteapp.notefeature.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ch.kra.noteapp.notefeature.domain.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao
}