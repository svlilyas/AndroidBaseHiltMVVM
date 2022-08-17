package com.pi.androidbasehiltmvvm.features.reading.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pi.androidbasehiltmvvm.core.db.AppDao
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.features.reading.domain.usecase.ReadingUseCase
import com.pi.androidbasehiltmvvm.features.reading.domain.viewevent.ReadingViewEvent
import com.pi.androidbasehiltmvvm.features.reading.domain.viewstate.ReadingViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val useCase: ReadingUseCase, private val appDB: AppDao
) : BaseViewModel<ReadingViewState, ReadingViewEvent>(ReadingViewState()) {

    private val _event = MutableLiveData<Event<ReadingViewEvent>>()
    val event: LiveData<Event<ReadingViewEvent>>
        get() = _event

    private val _serviceNumber = MutableLiveData<String>()
    val serviceNumber: LiveData<String>
        get() = _serviceNumber

    private val _currentReading = MutableLiveData<Double>()
    val currentReading: LiveData<Double>
        get() = _currentReading

    private fun sendEvent(event: ReadingViewEvent) = _event.postValue(Event(event))

    fun navigateToResultPage() {
        sendEvent(ReadingViewEvent.NavigateToResultPage)
    }

    fun getIfHasRecord(customerId: String) = appDB.getIfHasRecord(customerId = customerId)

    override fun onReduceState(viewAction: ReadingViewEvent): ReadingViewState =
        when (viewAction) {
            is ReadingViewEvent.NavigateToResultPage -> state.copy(
                isLoading = false,
                isError = false,
                albumName = "name"
            )
        }
}
