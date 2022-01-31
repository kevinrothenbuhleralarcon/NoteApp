package ch.kra.noteapp.notefeature.presentation.listnote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.kra.noteapp.databinding.NoteListItemLinearBinding
import ch.kra.noteapp.notefeature.domain.model.Note

class ListNoteAdapter(val viewModel: ListNoteViewModel): ListAdapter<Note,
        ListNoteAdapter.NoteViewHolder>(DiffCallBack) {
    companion object DiffCallBack: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }

    class NoteViewHolder(private val binding: NoteListItemLinearBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.note = note
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val binding = NoteListItemLinearBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.viewModel = viewModel
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}