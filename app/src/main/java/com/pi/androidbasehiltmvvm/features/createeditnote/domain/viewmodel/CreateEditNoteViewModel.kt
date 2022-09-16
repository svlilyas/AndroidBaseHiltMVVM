package com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewmodel

import androidx.lifecycle.viewModelScope
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.core.utils.AppConstants.Companion.EMPTY_STRING
import com.pi.androidbasehiltmvvm.core.utils.AppConstants.Companion.ZERO_INT
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.usecase.CreateEditNoteUseCase
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewevent.CreateEditNoteViewEvent
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewstate.CreateEditNoteViewState
import com.pi.data.remote.response.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditNoteViewModel @Inject constructor(
    private val useCase: CreateEditNoteUseCase
) : BaseViewModel<CreateEditNoteViewState, CreateEditNoteViewEvent>(CreateEditNoteViewState()) {

    private val _event = MutableSharedFlow<Event<CreateEditNoteViewEvent>>()
    val event = _event.asSharedFlow()

    val _id = MutableStateFlow(ZERO_INT)

    val _imageUrl = MutableStateFlow(EMPTY_STRING)
    val imageUrl = _imageUrl.asStateFlow()

    val _title = MutableStateFlow(EMPTY_STRING)

    val _description = MutableStateFlow(EMPTY_STRING)

    private val _isNoteExist = MutableStateFlow(false)
    val isNoteExist = _isNoteExist.asStateFlow()

    private fun sendEvent(event: CreateEditNoteViewEvent) {

        viewModelScope.launch {
            _event.emit(Event(event))
        }
    }

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
        }
    }

    fun saveNoteAndNavigateToList(newNote: Note) {
        viewModelScope.launch {
            if (_isNoteExist.value) {
                useCase.updateNote(newNote).collect {}
            } else {
                useCase.insertNote(newNote).collect {}
            }
        }
        sendEvent(CreateEditNoteViewEvent.SaveNoteAndNavigateToList)
    }

    fun controlNoteDetails() {
        val newNote = Note(
            id = _id.value,
            title = _title.value,
            description = _description.value,
            imageUrl = _imageUrl.value
        )

        sendEvent(CreateEditNoteViewEvent.ControlNoteDetails(newNote = newNote))
    }

    override fun onReduceState(viewAction: CreateEditNoteViewEvent): CreateEditNoteViewState =
        when (viewAction) {
            CreateEditNoteViewEvent.SaveNoteAndNavigateToList -> state.copy(
                isLoading = false,
                isError = false,
                fakeData = "fakeData"
            )
            is CreateEditNoteViewEvent.ControlNoteDetails -> state.copy(
                isLoading = false,
                isError = false,
                fakeData = "fakeData"
            )
        }
}
