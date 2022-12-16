package com.pi.androidbasehiltmvvm.features.notelist.domain.viewmodel

import androidx.lifecycle.viewModelScope
import com.pi.androidbasehiltmvvm.core.common.data.UiState
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.features.notelist.domain.usecase.NoteListUseCase
import com.pi.androidbasehiltmvvm.features.notelist.domain.viewaction.NoteListAction
import com.pi.androidbasehiltmvvm.features.notelist.domain.viewstate.NoteListViewState
import com.pi.androidbasehiltmvvm.features.notelist.presentation.NoteListFragmentDirections
import com.pi.data.remote.response.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val useCase: NoteListUseCase
) : BaseViewModel<NoteListViewState, NoteListAction>(NoteListViewState()) {

    private val navDirections = NoteListFragmentDirections

    init {
        /*val fakeNoteList = arrayListOf(
            Note(
                title = "note 1",
                description = "desc 1",
                imageUrl = "https://wallpaperaccess.com/full/2029165.jpg"
            ),
            Note(
                title = "note 2",
                description = "desc 2",
                imageUrl = "https://wallpaperaccess.com/full/2029165.jpg"
            ),
            Note(
                title = "note 3",
                description = "desc 3",
                imageUrl = "https://wallpaperaccess.com/full/2029165.jpg"
            ),
            Note(
                title = "note 4",
                description = "desc 4",
                imageUrl = "https://wallpaperaccess.com/full/2029165.jpg"
            ),
            Note(
                title = "note 5",
                description = "desc 5",
                imageUrl = "https://wallpaperaccess.com/full/2029165.jpg"
            )
        )

        fakeNoteList.forEach { note ->
            viewModelScope.launch {
                useCase.insertNote(note).collect {}
            }
        }*/
    }

    fun navigateToCreateNote() {
        val direction = navDirections.actionNoteListFragmentToCreateEditNoteFragment(
            note = null,
            isNoteExist = false
        )

        navigate(direction)
    }

    fun navigateToEditNote(note: Note) {
        val direction = navDirections.actionNoteListFragmentToCreateEditNoteFragment(
            note = note,
            isNoteExist = true
        )
        navigate(direction)
    }

    fun getAllNotes() {
        viewModelScope.launch {
            useCase.getAllNotes().collect { list ->
                sendAction(NoteListAction.GetNoteListSuccess(list))
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            useCase.deleteNote(note = note).collect {}
        }
    }

    override fun onReduceState(viewAction: NoteListAction): NoteListViewState =
        when (viewAction) {
            is NoteListAction.GetNoteListSuccess -> state.copy(
                noteList = viewAction.noteList,
                uiState = UiState.SUCCESS
            )
            NoteListAction.GetNoteListFailure -> state.copy(
                noteList = emptyList(),
                uiState = UiState.ERROR
            )
        }
}
