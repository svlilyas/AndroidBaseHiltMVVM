package com.pi.androidbasehiltmvvm.features.result.domain.viewstate

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState

data class ResultViewState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val albumName: String = ""
) : BaseViewState
