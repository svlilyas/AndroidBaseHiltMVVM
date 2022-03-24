package com.pi.androidbasehiltmvvm.features.customer.domain.viewevent

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewEvent

sealed class CustomerDashboardViewEvent : BaseViewEvent {
    object GenerateQrCode : CustomerDashboardViewEvent()
}
