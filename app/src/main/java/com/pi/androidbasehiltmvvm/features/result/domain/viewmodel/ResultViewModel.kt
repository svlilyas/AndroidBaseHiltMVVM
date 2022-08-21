package com.pi.androidbasehiltmvvm.features.result.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pi.androidbasehiltmvvm.core.extensions.Event
import com.pi.androidbasehiltmvvm.core.models.CustomerReading
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.core.utils.AppConstants
import com.pi.androidbasehiltmvvm.features.result.domain.usecase.ResultUseCase
import com.pi.androidbasehiltmvvm.features.result.domain.viewevent.ResultViewEvent
import com.pi.androidbasehiltmvvm.features.result.domain.viewstate.ResultViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val useCase: ResultUseCase
) : BaseViewModel<ResultViewState, ResultViewEvent>(ResultViewState()) {
    private val _event = MutableLiveData<Event<ResultViewEvent>>()
    val event: LiveData<Event<ResultViewEvent>>
        get() = _event

    private val _cost = MutableLiveData<Float>(1000.0F)
    val cost: LiveData<Float>
        get() = _cost

    private val _reading = MutableLiveData<Float>(0.0F)
    val reading: LiveData<Float>
        get() = _reading

    private val _customerId = MutableLiveData<String>()
    val customerId: LiveData<String>
        get() = _customerId

    private fun sendEvent(event: ResultViewEvent) {
        _event.postValue(Event(event))
    }

    fun navigateToReadingPage() {
        sendEvent(ResultViewEvent.NavigateReadingPage)
    }

    override fun onReduceState(viewAction: ResultViewEvent): ResultViewState =
        when (viewAction) {
            ResultViewEvent.NavigateReadingPage -> state.copy(
                isLoading = false,
                isError = false,
                albumName = "name"
            )
        }

    fun calculateCost(serviceNumber: String, remainingReading: Float, currentReading: Float) {
        _customerId.postValue(serviceNumber)
        _reading.postValue(currentReading)
        val resultCost = when (remainingReading) {
            in (0.0F)..(100.0F) -> remainingReading * AppConstants.COST_1
            in (100F)..(500.0F) -> (100 * AppConstants.COST_1).plus(remainingReading.minus(100) * AppConstants.COST_2)
            in 500.0F..Float.MAX_VALUE -> (100 * AppConstants.COST_1).plus(400 * AppConstants.COST_2)
                .plus(remainingReading.minus(500) * AppConstants.COST_3)
            else -> -1.0F
        }

        _cost.postValue(resultCost)
    }
}
