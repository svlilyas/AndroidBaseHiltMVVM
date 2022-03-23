package com.pi.androidbasehiltmvvm.features.customer.domain.viewevent

sealed class CustomerDashboardViewEvent {
    object GenerateQrCode : CustomerDashboardViewEvent()
}
