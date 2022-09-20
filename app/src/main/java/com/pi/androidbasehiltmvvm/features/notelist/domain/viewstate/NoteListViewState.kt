package com.pi.androidbasehiltmvvm.features.notelist.domain.viewstate

import com.pi.androidbasehiltmvvm.core.common.data.UiState
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState
import com.pi.data.remote.response.Note

data class NoteListViewState(
    val noteList: List<Note> = emptyList(),
    val note: Note? = null,
    override val uiState: UiState = UiState.LOADING
) : BaseViewState
