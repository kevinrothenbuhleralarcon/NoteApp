package ch.kra.noteapp.notefeature.presentation.listnote

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
        ListNoteViewModelFactory(
            (requireActivity().application as NoteApplication).noteRepository,
            (requireActivity().application as NoteApplication).dataStore
        )
    }

    private var isLinearLayoutManager: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
            recyclerView.adapter = ListNoteAdapter(this@ListNoteFragment.viewModel)
        }
        observeLayout()
        collectUIEvent()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)
        val menuItem = menu.findItem(R.id.action_switch_layout)
        setIcon(menuItem)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                viewModel.onEvent(ListNoteEvent.OnLayoutChanged(!isLinearLayoutManager))
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setIcon(menuItem: MenuItem?) {
        menuItem?.let {
            it.icon = if (isLinearLayoutManager) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid_layout)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_linear_layout)
            }
        }
    }

    private fun observeLayout() {
        viewModel.isLinearLayoutSelected.observe(viewLifecycleOwner) { value ->
            isLinearLayoutManager = value
            choseLayout()
            activity?.invalidateOptionsMenu()
        }
    }

    private fun choseLayout() {
        if (isLinearLayoutManager) {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }
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
                                getString(event.message),
                                Snackbar.LENGTH_LONG
                            )
                            event.action?.let { action ->
                                snackbar.setAction(getString(action)) {
                                    viewModel.onEvent(ListNoteEvent.UndoDelete)
                                }
                            }
                            snackbar.show()
                        }
                    }
                }
            }
        }
    }
}