package com.pi.androidbasehiltmvvm.features.notelist.domain.viewaction

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseAction
import com.pi.data.remote.response.Note

sealed class NoteListAction : BaseAction {
    class GetNoteListSuccess(val noteList: List<Note>) : NoteListAction()
    object GetNoteListFailure : NoteListAction()
}
