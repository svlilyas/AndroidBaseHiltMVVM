package com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewmodel

import androidx.lifecycle.viewModelScope
import com.pi.androidbasehiltmvvm.core.common.data.UiState
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.core.utils.AppConstants.Companion.STRING_EMPTY
import com.pi.androidbasehiltmvvm.core.utils.AppConstants.Companion.ZERO_INT
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.usecase.CreateEditNoteUseCase
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewaction.CreateEditNoteAction
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewstate.CreateEditNoteViewState
import com.pi.androidbasehiltmvvm.features.createeditnote.presentation.CreateEditNoteFragmentDirections
import com.pi.data.remote.response.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditNoteViewModel @Inject constructor(
    private val useCase: CreateEditNoteUseCase
) : BaseViewModel<CreateEditNoteViewState, CreateEditNoteAction>(CreateEditNoteViewState()) {

    private val navDirections = CreateEditNoteFragmentDirections

    val _id = MutableStateFlow(ZERO_INT)

    val _imageUrl = MutableStateFlow(STRING_EMPTY)
    val imageUrl = _imageUrl.asStateFlow()

    val _title = MutableStateFlow(STRING_EMPTY)

    val _description = MutableStateFlow(STRING_EMPTY)

    private val _isNoteExist = MutableStateFlow(false)
    val isNoteExist = _isNoteExist.asStateFlow()

    fun setViewData(note: Note?, isExist: Boolean) {

        viewModelScope.launch {
            if (isExist && note != null) {
                with(note) {
                    _id.emit(id)
                    _imageUrl.emit(imageUrl)
                    _title.emit(title)
                    _description.emit(description)
                }
            }
            _isNoteExist.emit(isExist)
            sendAction(CreateEditNoteAction.NoteDetailLoadSuccess(note = note))
        }
    }

    /**
     * Saving or Updating the note
     */
    fun saveNoteAndNavigateToList(newNote: Note) {
        viewModelScope.launch {
            if (_isNoteExist.value) {
                useCase.updateNote(newNote).collect {}
            } else {
                useCase.insertNote(newNote).collect {}
            }
        }

        val direction = navDirections.actionCreateEditNoteFragmentToNoteListFragment()
        navigate(direction)
    }

    fun controlNoteDetails() {
        val newNote = Note(
            id = _id.value,
            title = _title.value,
            description = _description.value,
            imageUrl = _imageUrl.value
        )

        sendAction(CreateEditNoteAction.ControlNoteDetails(newNote = newNote))
    }

    override fun onReduceState(viewAction: CreateEditNoteAction): CreateEditNoteViewState =
        when (viewAction) {
            is CreateEditNoteAction.NoteDetailLoadSuccess -> state.copy(
                newNote = null,
                sentNote = viewAction.note,
                uiState = UiState.SUCCESS
            )

            is CreateEditNoteAction.ControlNoteDetails -> state.copy(
                newNote = viewAction.newNote
            )
            CreateEditNoteAction.NoteDetailLoadError -> state.copy(
                sentNote = null,
                newNote = null,
                uiState = UiState.ERROR
            )
        }
}
