package com.pi.androidbasehiltmvvm.features.cashier.domain.viewstate

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState

data class CashierDashboardViewState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val albumName: String = ""
) : BaseViewState
