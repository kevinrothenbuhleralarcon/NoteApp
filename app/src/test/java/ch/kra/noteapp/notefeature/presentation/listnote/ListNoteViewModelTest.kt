package ch.kra.noteapp.notefeature.presentation.listnote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.kra.noteapp.core.UIEvent
import ch.kra.noteapp.notefeature.core.getOrAwaitValue
import ch.kra.noteapp.notefeature.data.preferences.SettingsDataStore
import ch.kra.noteapp.notefeature.data.repository.FakeNoteRepository
import ch.kra.noteapp.notefeature.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ListNoteViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private val data = listOf(
        Note(id = 1, title = "title 1", content = "content 1"),
        Note(id = 2, title = "title 2", content = "content 2"),
        Note(id = 3, title = "title 3", content = "content 3"),
    )
    lateinit var viewModel: ListNoteViewModel

    @Before
    fun setUp() {

        val fakeRepository = FakeNoteRepository(data as MutableList<Note>)
        val dataStore = SettingsDataStore(ApplicationProvider.getApplicationContext())
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = ListNoteViewModel(fakeRepository, dataStore)
    }

    @Test
    fun checkNoteList() {
        val notes = viewModel.notes.getOrAwaitValue()
        assertEquals("ListNoteViewModel.notes is not the correct size", data.size, notes.size)
    }

    @Test
    fun checkAddNoteEvent() = runBlockingTest {
        viewModel.onEvent(ListNoteEvent.OnAddNoteClicked)
        val event = viewModel.uiEvent.first()
        val isNavigateEvent = event is UIEvent.Navigate
        assertEquals("Event is not the correct one", true, isNavigateEvent)
    }

    @Test
    fun checkDeleteNote() {
        runBlockingTest {
            viewModel.onEvent(ListNoteEvent.OnDeleteNoteClicked(data[0]))
            val event = viewModel.uiEvent.first()
            val isEventShowSnackBar = event is UIEvent.ShowSnackbar
            assertEquals("Event is not ShowSnackbar", true, isEventShowSnackBar)
            val notes = viewModel.notes.getOrAwaitValue()
            assertEquals("ListNoteViewModel.notes is not the correct size", data.size - 1 , notes.size)
        }
    }
}