package com.pi.androidbasehiltmvvm.features.customer.domain.viewstate

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState

data class CustomerDashboardViewState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val albumName: String = ""
) : BaseViewState
