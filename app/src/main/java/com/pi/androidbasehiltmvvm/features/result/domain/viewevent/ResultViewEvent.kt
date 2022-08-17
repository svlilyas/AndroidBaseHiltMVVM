package com.pi.androidbasehiltmvvm.features.result.domain.viewevent

import com.pi.androidbasehiltmvvm.core.platform.viewmodel.BaseViewEvent

sealed class ResultViewEvent : BaseViewEvent {
    object NavigateReadingPage : ResultViewEvent()
}
