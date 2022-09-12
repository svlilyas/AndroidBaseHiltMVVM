package com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.extensions.asLiveData
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.core.utils.AppConstants.Companion.EMPTY_STRING
import com.pi.androidbasehiltmvvm.core.utils.AppConstants.Companion.ZERO_INT
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.usecase.CreateEditNoteUseCase
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewevent.CreateEditNoteViewEvent
import com.pi.data.remote.response.Note
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewstate.CreateEditNoteViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditNoteViewModel @Inject constructor(
    private val useCase: CreateEditNoteUseCase
) : BaseViewModel<CreateEditNoteViewState, CreateEditNoteViewEvent>(CreateEditNoteViewState()) {

    private val _event = MutableLiveData<Event<CreateEditNoteViewEvent>>()
    val event = _event.asLiveData()

    val _id = MutableLiveData(ZERO_INT)

    val _imageUrl = MutableLiveData(EMPTY_STRING)
    val imageUrl = _imageUrl.asLiveData()

    val _title = MutableLiveData(EMPTY_STRING)

    val _description = MutableLiveData(EMPTY_STRING)

    private val _isNoteExist = MutableLiveData(false)
    val isNoteExist = _isNoteExist.asLiveData()

    private fun sendEvent(event: CreateEditNoteViewEvent) = _event.postValue(Event(event))

    fun setViewData(note: Note?, isExist: Boolean) {
        if (isExist && note != null) {
            with(note) {
                _id.postValue(id)
                _imageUrl.postValue(imageUrl)
                _title.postValue(title)
                _description.postValue(description)
            }
        }
        _isNoteExist.postValue(isExist)
    }

    fun saveNoteAndNavigateToList(newNote: Note) {
        viewModelScope.launch {
            if (_isNoteExist.value == true) {
                useCase.updateNote(newNote).collect {}
            } else {
                useCase.insertNote(newNote).collect {}
            }
        }
        sendEvent(CreateEditNoteViewEvent.SaveNoteAndNavigateToList)
    }

    fun controlNoteDetails() {
        val newNote = Note(
            id = _id.value ?: 0,
            title = _title.value.toString(),
            description = _description.value.toString(),
            imageUrl = _imageUrl.value.toString()
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
