package ch.kra.noteapp.notefeature.data.repository

import ch.kra.noteapp.notefeature.domain.model.Note
import ch.kra.noteapp.notefeature.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteRepository(private var notes: MutableList<Note> = mutableListOf()): NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> {
        return flow {
            emit(notes)
        }
    }

    override suspend fun getNoteById(noteId: Int): Note? {
        return notes.find { it.id == noteId }
    }

    override suspend fun insertNote(note: Note) {
        notes.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
    }

    override suspend fun updateNote(note: Note) {
        val noteToUpdate = notes.find { it.id == note.id }
        noteToUpdate?.let {
            notes.remove(it)
        }
        notes.add(note)
    }
}