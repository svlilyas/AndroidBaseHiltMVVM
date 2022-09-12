package com.pi.androidbasehiltmvvm.features.notelist.domain.viewevent

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewEvent
import com.pi.data.remote.response.Note

sealed class NoteListViewEvent : BaseViewEvent {

    object NavigateToCreateNote : NoteListViewEvent()
    class NavigateToEditNote(val note: Note) : NoteListViewEvent()
}
