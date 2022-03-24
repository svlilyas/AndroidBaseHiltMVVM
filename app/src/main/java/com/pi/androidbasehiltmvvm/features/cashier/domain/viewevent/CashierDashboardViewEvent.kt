package com.pi.androidbasehiltmvvm.features.cashier.domain.viewevent

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewEvent

sealed class CashierDashboardViewEvent : BaseViewEvent {
    data class NavigateToHome(val name: String) : CashierDashboardViewEvent()
}
