package com.pi.androidbasehiltmvvm.features.cashier.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.features.cashier.domain.usecase.CashierDashboardUseCase
import com.pi.androidbasehiltmvvm.features.cashier.domain.viewevent.CashierDashboardViewEvent
import com.pi.androidbasehiltmvvm.features.cashier.domain.viewstate.CashierDashboardViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CashierDashboardViewModel @Inject constructor(
    private val useCase: CashierDashboardUseCase
) : BaseViewModel<CashierDashboardViewState, CashierDashboardViewEvent>(CashierDashboardViewState()) {

    private val _event = MutableLiveData<Event<CashierDashboardViewEvent>>()
    val event: LiveData<Event<CashierDashboardViewEvent>>
        get() = _event

    private val _cameraPermissionGranted = MutableLiveData<Boolean>()
    val cameraPermissionGranted: LiveData<Boolean>
        get() = _cameraPermissionGranted

    private fun sendEvent(event: CashierDashboardViewEvent) {
        _event.postValue(Event(event))
    }

    override fun onReduceState(viewAction: CashierDashboardViewEvent): CashierDashboardViewState =
        when (viewAction) {
            is CashierDashboardViewEvent.NavigateToHome -> state.copy(
                isLoading = false,
                isError = false,
                albumName = "name"
            )
        }
}
