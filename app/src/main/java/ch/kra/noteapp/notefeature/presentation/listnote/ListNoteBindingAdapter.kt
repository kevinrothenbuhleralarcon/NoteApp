package ch.kra.noteapp.notefeature.presentation.listnote

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.kra.noteapp.notefeature.domain.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("app:viewModel", "app:note")
fun setClick(view: ConstraintLayout, viewModel: ListNoteViewModel, note: Note) {
    view.setOnClickListener {
        viewModel.onEvent(ListNoteEvent.OnNoteClicked(note))
    }
}

@BindingAdapter("app:notes")
fun setNotes(listView: RecyclerView, notes: List<Note>?) {
    notes?.let {
        (listView.adapter as ListNoteAdapter).submitList(it)
    }
}

@BindingAdapter("app:viewModel", "app:note")
fun setClick(view: ImageView, viewModel: ListNoteViewModel, note: Note) {
    view.setOnClickListener {
        viewModel.onEvent(ListNoteEvent.OnDeleteNoteClicked(note))
    }
}

@BindingAdapter("app:viewModel")
fun setClick(view: FloatingActionButton, viewModel: ListNoteViewModel) {
    view.setOnClickListener {
        viewModel.onEvent(ListNoteEvent.OnAddNoteClicked)
    }
}