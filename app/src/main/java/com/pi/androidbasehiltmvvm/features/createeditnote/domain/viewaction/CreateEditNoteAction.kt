package com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewaction

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseAction
import com.pi.data.remote.response.Note

sealed class CreateEditNoteAction : BaseAction {
    class NoteDetailLoadSuccess(val note: Note?) : CreateEditNoteAction()
    object NoteDetailLoadError : CreateEditNoteAction()
    class ControlNoteDetails(val newNote: Note) : CreateEditNoteAction()
}
