package com.pi.androidbasehiltmvvm.features.reading.domain.viewstate

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState

data class ReadingViewState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val albumName: String = ""
) : BaseViewState
