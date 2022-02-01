package ch.kra.noteapp.notefeature.presentation.addupdatenote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import ch.kra.noteapp.NoteApplication
import ch.kra.noteapp.R
import ch.kra.noteapp.core.UIEvent
import ch.kra.noteapp.databinding.FragmentAddUpdateNoteBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddUpdateNoteFragment : Fragment() {

    private var _binding: FragmentAddUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddUpdateNoteViewModel> {
        AddUpdateNoteViewModel.AddUpdateNoteViewModelFactory((requireActivity().application as NoteApplication).noteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_update_note, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        arguments?.let {
            viewModel.loadNote(it.getInt("noteId"))
        }
        collectUIEvent()
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
                            Snackbar
                                .make(
                                    binding.root,
                                    getString(event.message),
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                }
            }
        }
    }
}