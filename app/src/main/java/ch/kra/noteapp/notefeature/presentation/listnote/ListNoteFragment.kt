package ch.kra.noteapp.notefeature.presentation.listnote

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ListNoteFragment : Fragment() {

    private var _binding: FragmentListNoteBinding? = null
    private val binding get() = _binding!!

    private val listNoteViewModel by viewModels<ListNoteViewModel> {
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
            viewModel = listNoteViewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = ListNoteAdapter(listNoteViewModel)
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
                listNoteViewModel.uiEvent.collect { event ->
                    when (event) {
                        is UIEvent.Navigate -> {
                            findNavController().navigate(event.destination)
                        }
                        is UIEvent.ShowSnackbar -> {

                        }
                        is UIEvent.NavigateBack -> {

                        }
                    }
                }
            }
        }
    }
}