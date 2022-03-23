package com.pi.androidbasehiltmvvm.features.homepage.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pi.androidbasehiltmvvm.core.common.PreferenceManager
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.platform.BaseViewModel
import com.pi.androidbasehiltmvvm.features.homepage.domain.usecase.HomePageUseCase
import com.pi.androidbasehiltmvvm.features.homepage.domain.viewevent.HomePageViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val useCase: HomePageUseCase,
    val preferenceManager: PreferenceManager
) : BaseViewModel() {
    private val _event = MutableLiveData<Event<HomePageViewEvent>>()
    val event: LiveData<Event<HomePageViewEvent>>
        get() = _event

    private fun sendEvent(event: HomePageViewEvent) {
        _event.postValue(Event(event))
    }

    fun navigateToCustomerPage() {
        sendEvent(HomePageViewEvent.NavigateCustomerPage)
    }

    fun navigateToCashierPage() {
        sendEvent(HomePageViewEvent.NavigateCashierPage)
    }
}
