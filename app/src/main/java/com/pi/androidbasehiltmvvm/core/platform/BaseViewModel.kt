package com.pi.androidbasehiltmvvm.core.platform

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pi.androidbasehiltmvvm.core.extensions.Event
import retrofit2.HttpException
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    /**
     * [LiveData] that emits [ProgressState] to determine show/hide state of loading indicators(ie: HUD)
     */
    internal val progressStateObservable: MutableLiveData<ProgressState> by lazy {
        MutableLiveData()
    }

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _baseEvent = MutableLiveData<Event<BaseViewEvent>>()
    val baseEvent: LiveData<Event<BaseViewEvent>> = _baseEvent

    private val _isResponseEmpty = MutableLiveData(false)
    val isResponseEmpty: LiveData<Boolean> = _isResponseEmpty

    fun setLoading(loading: Boolean) = _loading.postValue(loading)

    fun setEmpty(isResponseEmpty: Boolean) = _isResponseEmpty.postValue(isResponseEmpty)

    internal fun emitProgressShow() {
        progressStateObservable.postValue(ProgressState.Show)
    }

    fun encodeBase64(string: String): String {
        return Base64.encodeToString(
            string.toByteArray(),
            Base64.NO_WRAP
        )
    }

    open fun handleException(e: Throwable?) {
        Timber.e(e)
        when (e) {
            is HttpException -> {
                when (e.code()) {
                    /*401, 462 -> forceLogout()

                    403 -> _baseEvent.postValue(Event(BaseViewEvent.ShowUserNotFoundError))*/
                    500 -> _baseEvent.postValue(Event(BaseViewEvent.ShowInternalServerError))
                    // else -> showCommonNetworkError()
                    /*else -> {
                        if (e.code() in 499..599) {
                            _baseEvent.postValue(Event(BaseViewEvent.ShowInternalServerError))
                        } else {
                            try {
                                Gson().fromJson(
                                    e.response()?.errorBody()?.string(),
                                    ErrorResponse::class.java
                                )?.errorMessage?.let {
                                    showCustomError(
                                        it
                                    )
                                }
                            } catch (exception: Exception) {
                                showCommonNetworkError()
                            }
                        }
                    }*/
                }
            }

            /*is JsonSyntaxException -> showCommonNetworkError()

            is UnknownHostException -> showCommonNetworkError()
            else -> showCommonNetworkError()*/
        }
    }

    /**
     * Used with [progressStateObservable] for emitting state to show/hide loading indicators.(ie: HUD)
     */
    sealed class ProgressState {
        object Show : ProgressState()
        object Hide : ProgressState()
    }
}
