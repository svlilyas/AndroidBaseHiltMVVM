package com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewstate

import com.pi.androidbasehiltmvvm.core.common.data.UiState
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState
import com.pi.data.remote.response.Note

data class CreateEditNoteViewState(
    val newNote: Note? = null,
    val sentNote: Note? = null,
    override val uiState: UiState = UiState.LOADING
) : BaseViewState
