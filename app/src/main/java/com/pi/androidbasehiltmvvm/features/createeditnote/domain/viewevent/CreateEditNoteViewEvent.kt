package com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewevent

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewEvent
import com.pi.data.remote.response.Note

sealed class CreateEditNoteViewEvent : BaseViewEvent {
    
    object SaveNoteAndNavigateToList : CreateEditNoteViewEvent()
    class ControlNoteDetails(val newNote: Note) : CreateEditNoteViewEvent()
}
