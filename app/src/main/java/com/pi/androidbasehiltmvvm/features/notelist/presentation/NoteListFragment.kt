package com.pi.androidbasehiltmvvm.features.notelist.presentation

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.extensions.observe
import com.pi.androidbasehiltmvvm.core.extensions.observeEvent
import com.pi.androidbasehiltmvvm.core.extensions.toast
import com.pi.androidbasehiltmvvm.core.navigation.PageName
import com.pi.androidbasehiltmvvm.core.platform.BaseFragment
import com.pi.androidbasehiltmvvm.core.utils.SwipeGesture
import com.pi.androidbasehiltmvvm.databinding.FragmentNoteListBinding
import com.pi.androidbasehiltmvvm.features.notelist.domain.viewevent.NoteListViewEvent
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
            noteAdapter = NoteAdapter(viewModel)
            notesRecyclerView.adapter = noteAdapter

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

        observeEvent(viewModel.event, ::onViewEvent)

        observe(viewModel.noteList) { noteList ->

            if (noteList.isNotEmpty()) {
                noteAdapter.submitList(noteList)
            }
        }
    }

    private fun onViewEvent(event: NoteListViewEvent) {
        when (event) {
            NoteListViewEvent.NavigateToCreateNote -> {
                findNavController().navigate(
                    NoteListFragmentDirections.actionNoteListFragmentToCreateEditNoteFragment(
                        note = null,
                        isNoteExist = false
                    )
                )
            }
            is NoteListViewEvent.NavigateToEditNote -> {
                findNavController().navigate(
                    NoteListFragmentDirections.actionNoteListFragmentToCreateEditNoteFragment(
                        note = event.note,
                        isNoteExist = true
                    )
                )
            }
        }
    }
}
