package com.pi.androidbasehiltmvvm.features.cashier.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pi.androidbasehiltmvvm.core.common.PreferenceManager
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.platform.BaseViewModel
import com.pi.androidbasehiltmvvm.features.cashier.domain.usecase.CashierDashboardUseCase
import com.pi.androidbasehiltmvvm.features.cashier.domain.viewevent.CashierDashboardViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CashierDashboardViewModel @Inject constructor(
    private val useCase: CashierDashboardUseCase,
    val preferenceManager: PreferenceManager
) : BaseViewModel() {

    private val _event = MutableLiveData<Event<CashierDashboardViewEvent>>()
    val event: LiveData<Event<CashierDashboardViewEvent>>
        get() = _event

    private val _cameraPermissionGranted = MutableLiveData<Boolean>()
    val cameraPermissionGranted: LiveData<Boolean>
        get() = _cameraPermissionGranted

    private fun sendEvent(event: CashierDashboardViewEvent) {
        _event.postValue(Event(event))
    }
}
