package com.pi.androidbasehiltmvvm.features.homepage.domain.viewevent

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewEvent

sealed class HomePageViewEvent : BaseViewEvent {
    object NavigateCustomerPage : HomePageViewEvent()
    object NavigateCashierPage : HomePageViewEvent()
}
