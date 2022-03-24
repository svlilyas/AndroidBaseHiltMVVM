package com.pi.androidbasehiltmvvm.features.homepage.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pi.androidbasehiltmvvm.core.common.PreferenceManager
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.features.homepage.domain.usecase.HomePageUseCase
import com.pi.androidbasehiltmvvm.features.homepage.domain.viewevent.HomePageViewEvent
import com.pi.androidbasehiltmvvm.features.homepage.domain.viewstate.HomePageViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val useCase: HomePageUseCase,
    val preferenceManager: PreferenceManager
) : BaseViewModel<HomePageViewState, HomePageViewEvent>(HomePageViewState()) {
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

    override fun onReduceState(viewAction: HomePageViewEvent): HomePageViewState =
        when (viewAction) {
            HomePageViewEvent.NavigateCashierPage -> state.copy(
                isLoading = false,
                isError = false,
                albumName = "name"
            )
            HomePageViewEvent.NavigateCustomerPage -> state.copy(
                isLoading = false,
                isError = false,
                albumName = "name"
            )
        }
}
