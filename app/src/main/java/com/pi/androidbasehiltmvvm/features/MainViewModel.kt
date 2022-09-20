package com.pi.androidbasehiltmvvm.features

import com.pi.androidbasehiltmvvm.core.common.data.UiState
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseAction
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewModel
import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() :
    BaseViewModel<MainViewModel.ViewState, MainViewModel.Action>(ViewState()) {

    data class ViewState(
        val data: String = "",
        override val uiState: UiState = UiState.SUCCESS
    ) : BaseViewState

    sealed interface Action : BaseAction {
        object AlbumListLoadingFailure : Action
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        Action.AlbumListLoadingFailure -> state.copy(
            uiState = UiState.ERROR
        )
    }
}
