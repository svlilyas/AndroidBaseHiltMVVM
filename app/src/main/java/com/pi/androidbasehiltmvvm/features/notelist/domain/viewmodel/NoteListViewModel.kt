package com.pi.androidbasehiltmvvm.features.notelist.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.extensions.asLiveData
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.features.notelist.domain.usecase.NoteListUseCase
import com.pi.androidbasehiltmvvm.features.notelist.domain.viewevent.NoteListViewEvent
import com.pi.data.remote.response.Note
import com.pi.androidbasehiltmvvm.features.notelist.domain.viewstate.NoteListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val useCase: NoteListUseCase
) : BaseViewModel<NoteListViewState, NoteListViewEvent>(NoteListViewState()) {

    private val _event = MutableLiveData<Event<NoteListViewEvent>>()
    val event = _event.asLiveData()

    private val _noteList = MutableLiveData<List<Note>>()
    val noteList = _noteList.asLiveData()

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

    private fun sendEvent(event: NoteListViewEvent) = _event.postValue(Event(event))

    fun navigateToCreateNote() {
        sendEvent(NoteListViewEvent.NavigateToCreateNote)
    }

    fun navigateToEditNote(note: Note) {
        sendEvent(NoteListViewEvent.NavigateToEditNote(note = note))
    }

    fun getAllNotes() {
        viewModelScope.launch {
            useCase.getAllNotes().collect { list ->
                _noteList.postValue(list)
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            useCase.deleteNote(note = note).collect {}
        }
    }

    override fun onReduceState(viewAction: NoteListViewEvent): NoteListViewState =
        when (viewAction) {
            is NoteListViewEvent.NavigateToEditNote -> state.copy(
                isLoading = false,
                isError = false,
                fakeData = "fakeData"
            )
            NoteListViewEvent.NavigateToCreateNote -> state.copy(
                isLoading = false,
                isError = false,
                fakeData = "fakeData"
            )
        }
}
