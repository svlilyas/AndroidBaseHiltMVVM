package com.pi.androidbasehiltmvvm.features.notelist.domain.viewstate

import com.pi.androidbasehiltmvvm.core.common.data.UiState
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState

data class NoteListViewState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val fakeData: String = ""
) : BaseViewState {
    override val viewState: UiState
        get() = UiState.LOADING
}
