package com.pi.androidbasehiltmvvm.features.notelist.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.binding.ViewBinding.visible
import com.pi.androidbasehiltmvvm.core.extensions.changeUiState
import com.pi.androidbasehiltmvvm.core.extensions.observe
import com.pi.androidbasehiltmvvm.core.extensions.toast
import com.pi.androidbasehiltmvvm.core.platform.delegations.viewBinding
import com.pi.androidbasehiltmvvm.core.platform.delegations.viewModelWithNavigation
import com.pi.androidbasehiltmvvm.core.utils.SwipeGesture
import com.pi.androidbasehiltmvvm.databinding.FragmentNoteListBinding
import com.pi.androidbasehiltmvvm.features.notelist.domain.viewmodel.NoteListViewModel
import com.pi.androidbasehiltmvvm.features.notelist.presentation.adapter.NoteAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A @Fragment for showing all Notes
 */
@AndroidEntryPoint
class NoteListFragment : Fragment(R.layout.fragment_note_list) {
    private val binding by viewBinding(FragmentNoteListBinding::bind)
    private val viewModel by viewModelWithNavigation<NoteListViewModel>()

    private var noteAdapter: NoteAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
        getViewData()
        observeData()
    }

    /**
     * Initialize view informations and observing recyclerview gestures
     */
    private fun setUpViews() {
        binding.apply {
            vm = viewModel

            // Setup Adapter with Recyclerview
            noteAdapter = NoteAdapter()
            notesRecyclerView.setHasFixedSize(true)
            notesRecyclerView.adapter = noteAdapter

            /**
             * Adapter click listeners
             */
            noteAdapter?.setOnDebouncedClickListener { note ->
                viewModel.navigateToEditNote(note = note)
            }

            initializeRecyclerViewGesture()
        }
    }

    /**
     * Adding and listening Swipe to Left and Right gestures to remove note item from list and AppDb
     */
    private fun initializeRecyclerViewGesture() {
        val swipeGesture = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    val removedItem = noteAdapter?.removeItem(viewHolder.absoluteAdapterPosition)
                    if (removedItem != null) {
                        viewModel.deleteNote(removedItem)
                    }
                    requireContext().toast(R.string.note_removed)
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.notesRecyclerView)
    }

    /**
     * Get all Notes from AppDb
     */
    private fun getViewData() {
        viewModel.getAllNotes()
    }

    override fun onDestroyView() {
        noteAdapter = null
        super.onDestroyView()
    }

    /**
     * Observing View Events and noteList to fill the View
     */
    private fun observeData() {

        observe(viewModel.uiStateFlow) { viewState ->
            binding.apply {
                progressBar.changeUiState(uiState = viewState.uiState)

                notesRecyclerView.visible = viewState.noteList.isNotEmpty()

                noteAdapter?.submitList(viewState.noteList)
            }
        }
    }
}
