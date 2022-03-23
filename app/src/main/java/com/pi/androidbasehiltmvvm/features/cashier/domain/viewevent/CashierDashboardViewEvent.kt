package com.pi.androidbasehiltmvvm.features.cashier.domain.viewevent

sealed class CashierDashboardViewEvent {
    object ReadQrCode : CashierDashboardViewEvent()
}
