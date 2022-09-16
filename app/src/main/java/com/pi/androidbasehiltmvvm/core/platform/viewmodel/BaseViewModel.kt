package com.pi.androidbasehiltmvvm.core.platform.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.androidbasehiltmvvm.BuildConfig
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

/**
 * Base ViewModel to manage to put all common methods and manage
 * @param ViewState is for storing state of the view
 * @param ViewAction is for storing all actions about the view
 */
abstract class BaseViewModel<ViewState : BaseViewState, ViewAction : BaseViewEvent>(initialState: ViewState) :
    ViewModel() {

    /**
     * For storing ViewState
     */
    private val stateMutableLiveData = MutableSharedFlow<ViewState>()
    val stateLiveData = stateMutableLiveData.asSharedFlow()

    private var stateTimeTravelDebugger: StateTimeTravelDebugger? = null

    init {
        if (BuildConfig.DEBUG) {
            stateTimeTravelDebugger = StateTimeTravelDebugger(this::class.java.simpleName)
        }
    }

    /**
     * Delegate will handle state event deduplication
     * (multiple states of the same type holding the same data
     * will not be dispatched multiple times to LiveData stream)
     */
    protected var state by Delegates.observable(initialState) { _, old, new ->

        viewModelScope.launch {
            stateMutableLiveData.emit(new)
        }

        if (new != old) {
            stateTimeTravelDebugger?.apply {
                addStateTransition(old, new)
                logLast()
            }
        }
    }

    fun sendAction(viewAction: ViewAction) {
        stateTimeTravelDebugger?.addAction(viewAction)
        state = onReduceState(viewAction)
    }

    fun loadData() {
        onLoadData()
    }

    protected open fun onLoadData() {}

    protected abstract fun onReduceState(viewAction: ViewAction): ViewState
}
