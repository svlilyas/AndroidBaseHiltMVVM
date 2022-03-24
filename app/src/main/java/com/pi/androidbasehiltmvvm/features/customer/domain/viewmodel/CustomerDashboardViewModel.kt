package com.pi.androidbasehiltmvvm.features.customer.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pi.androidbasehiltmvvm.core.common.PreferenceManager
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.features.customer.domain.usecase.CustomerDashboardUseCase
import com.pi.androidbasehiltmvvm.features.customer.domain.viewevent.CustomerDashboardViewEvent
import com.pi.androidbasehiltmvvm.features.customer.domain.viewstate.CustomerDashboardViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerDashboardViewModel @Inject constructor(
    private val useCase: CustomerDashboardUseCase,
    val preferenceManager: PreferenceManager
) : BaseViewModel<CustomerDashboardViewState, CustomerDashboardViewEvent>(CustomerDashboardViewState()) {
    private val _event = MutableLiveData<Event<CustomerDashboardViewEvent>>()
    val event: LiveData<Event<CustomerDashboardViewEvent>>
        get() = _event

    private fun sendEvent(event: CustomerDashboardViewEvent) {
        _event.postValue(Event(event))
    }

    fun generateQrCode() {
        sendEvent(CustomerDashboardViewEvent.GenerateQrCode)
    }

    override fun onReduceState(viewAction: CustomerDashboardViewEvent): CustomerDashboardViewState =
        when (viewAction) {
            is CustomerDashboardViewEvent.GenerateQrCode -> state.copy(
                isLoading = false,
                isError = false,
                albumName = "name"
            )
        }
}
