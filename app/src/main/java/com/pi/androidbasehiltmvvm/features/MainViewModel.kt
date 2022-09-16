package com.pi.androidbasehiltmvvm.features

import com.pi.androidbasehiltmvvm.core.common.data.UiState
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewEvent
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() :
    BaseViewModel<MainViewModel.ViewState, MainViewModel.ViewEvent>(ViewState()) {

    data class ViewState(
        val data: String = "",
        override val viewState: UiState = UiState.SUCCESS
    ) : BaseViewState

    sealed interface ViewEvent : BaseViewEvent {
        object AlbumListLoadingFailure : ViewEvent
    }

    override fun onReduceState(viewAction: ViewEvent): ViewState = when (viewAction) {
        ViewEvent.AlbumListLoadingFailure -> state.copy(
            viewState = UiState.ERROR
        )
    }
}
