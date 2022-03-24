package com.pi.androidbasehiltmvvm.features.homepage.domain.viewstate

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState

data class HomePageViewState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val albumName: String = ""
) : BaseViewState
