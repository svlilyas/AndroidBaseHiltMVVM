package com.pi.androidbasehiltmvvm.core.platform

sealed class BaseViewEvent {

    object ForceLogout : BaseViewEvent()

    object ShowCommonNetworkError : BaseViewEvent()

    object ShowConnectivityError : BaseViewEvent()

    object ShowUserNotFoundError : BaseViewEvent()

    object ShowInternalServerError : BaseViewEvent()

    data class ShowCustomError(val message: String) : BaseViewEvent()
}
