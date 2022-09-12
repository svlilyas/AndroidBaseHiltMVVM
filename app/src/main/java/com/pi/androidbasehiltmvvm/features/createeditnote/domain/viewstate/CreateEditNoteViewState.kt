package com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewstate

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState

data class CreateEditNoteViewState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val fakeData: String = ""
) : BaseViewState
