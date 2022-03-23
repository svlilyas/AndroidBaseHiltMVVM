package com.pi.androidbasehiltmvvm.features.homepage.domain.viewevent

sealed class HomePageViewEvent {
    object NavigateCustomerPage : HomePageViewEvent()
    object NavigateCashierPage : HomePageViewEvent()
}
