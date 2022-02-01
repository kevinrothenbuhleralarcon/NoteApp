package ch.kra.noteapp.notefeature.presentation.listnote

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.kra.noteapp.notefeature.data.preferences.SettingsDataStore
import ch.kra.noteapp.notefeature.data.repository.FakeNoteRepository
import ch.kra.noteapp.notefeature.domain.model.Note
import ch.kra.noteapp.notefeature.domain.repository.NoteRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class ListNoteAdapterTest {

    private val data = listOf(
        Note(id = 1, title = "title 1", content = "content 1"),
        Note(id = 2, title = "title 2", content = "content 2"),
        Note(id = 3, title = "title 3", content = "content 3"),
    )

    private lateinit var repository: NoteRepository
    private val dataSource = SettingsDataStore(ApplicationProvider.getApplicationContext())
    private lateinit var viewModel: ListNoteViewModel

    @Before
    fun setUp() {
        repository = FakeNoteRepository(data as MutableList<Note>)
        viewModel = ListNoteViewModel(repository, dataSource)
    }

    @Test
    fun adapter_size() {
        val adapter = ListNoteAdapter(viewModel)
        adapter.submitList(data)
        assertEquals("ListNoteAdapter is not the correct size", adapter.itemCount, data.size)
    }
}