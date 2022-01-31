package ch.kra.noteapp.notefeature.presentation.addupdatenote

import android.widget.Button
import androidx.databinding.BindingAdapter

@BindingAdapter("app:bindSaveNote")
fun onClickSaveNote(view: Button, viewModel: AddUpdateNoteViewModel) {
    view.setOnClickListener {
        viewModel.onEvent(AddUpdateNoteEvent.OnSaveNoteClicked)
    }
}

@BindingAdapter("app:bindUpdateNote")
fun onClickUpdateNote(view: Button, viewModel: AddUpdateNoteViewModel) {
    view.setOnClickListener {
        viewModel.onEvent(AddUpdateNoteEvent.OnUpdateNoteClicked)
    }
}