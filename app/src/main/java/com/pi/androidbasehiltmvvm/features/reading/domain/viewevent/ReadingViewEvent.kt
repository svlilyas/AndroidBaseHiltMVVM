package com.pi.androidbasehiltmvvm.features.reading.domain.viewevent

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewEvent

sealed class ReadingViewEvent : BaseViewEvent {
    object NavigateToResultPage : ReadingViewEvent()
}
