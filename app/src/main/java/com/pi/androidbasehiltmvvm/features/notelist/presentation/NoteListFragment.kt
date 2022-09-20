package com.pi.androidbasehiltmvvm.features.notelist.presentation

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.binding.ViewBinding.visible
import com.pi.androidbasehiltmvvm.core.extensions.changeUiState
import com.pi.androidbasehiltmvvm.core.extensions.observe
import com.pi.androidbasehiltmvvm.core.extensions.toast
import com.pi.androidbasehiltmvvm.core.navigation.PageName
import com.pi.androidbasehiltmvvm.core.platform.BaseFragment
import com.pi.androidbasehiltmvvm.core.utils.SwipeGesture
import com.pi.androidbasehiltmvvm.databinding.FragmentNoteListBinding
import com.pi.androidbasehiltmvvm.features.notelist.domain.viewmodel.NoteListViewModel
import com.pi.androidbasehiltmvvm.features.notelist.presentation.adapter.NoteAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A @Fragment for showing all Notes
 */
@AndroidEntryPoint
class NoteListFragment :
    BaseFragment<FragmentNoteListBinding, NoteListViewModel>(
        layoutId = R.layout.fragment_note_list,
        viewModelClass = NoteListViewModel::class.java
    ) {
    /**
     * To know which page we are in - It might use in @DeepLink Feature
     */
    override fun getScreenKey(): String = PageName.PreLogin.NOTE_CREATE_EDIT_PAGE

    private lateinit var noteAdapter: NoteAdapter

    /**
     * Initialize view informations and observing recyclerview gestures
     */
    override fun setUpViews() {
        super.setUpViews()
        binding.apply {
            viewmodel = viewModel

            // Setup Adapter with Recyclerview
            noteAdapter = NoteAdapter()
            notesRecyclerView.setHasFixedSize(true)
            notesRecyclerView.adapter = noteAdapter

            /**
             * Adapter click listeners
             */
            noteAdapter.setOnDebouncedClickListener { note ->
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
                    val removedItem = noteAdapter.removeItem(viewHolder.absoluteAdapterPosition)
                    viewModel.deleteNote(removedItem)
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
    override fun getViewData() {
        viewModel.getAllNotes()
    }

    /**
     * Observing View Events and noteList to fill the View
     */
    override fun observeData() {

        observe(viewModel.uiStateFlow) { viewState ->
            binding.apply {
                progressBar.changeUiState(uiState = viewState.uiState)

                notesRecyclerView.visible = viewState.noteList.isNotEmpty()

                noteAdapter.submitList(viewState.noteList)
            }
        }
    }
}
