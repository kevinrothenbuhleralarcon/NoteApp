package ch.kra.noteapp.notefeature.presentation.listnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ch.kra.noteapp.NoteApplication
import ch.kra.noteapp.R
import ch.kra.noteapp.core.UIEvent
import ch.kra.noteapp.databinding.FragmentListNoteBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ListNoteFragment : Fragment() {

    private var _binding: FragmentListNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ListNoteViewModel> {
        ListNoteViewModelFactory((requireActivity().application as NoteApplication).noteRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_note, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = this@ListNoteFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = ListNoteAdapter(this@ListNoteFragment.viewModel)
        }
        collectUIEvent()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun collectUIEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is UIEvent.Navigate -> {
                            findNavController().navigate(event.destination)
                        }
                        is UIEvent.ShowSnackbar -> {
                            val snackbar = Snackbar.make(
                                binding.root,
                                event.message,
                                Snackbar.LENGTH_LONG
                            )
                            snackbar.setAction(event.action) {
                                viewModel.onEvent(ListNoteEvent.UndoDelete)
                            }
                            snackbar.show()
                        }
                    }
                }
            }
        }
    }
}