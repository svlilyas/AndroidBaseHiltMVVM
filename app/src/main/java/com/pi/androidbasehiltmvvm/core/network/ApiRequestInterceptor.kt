package com.pi.androidbasehiltmvvm.core.network

import com.pi.androidbasehiltmvvm.core.common.PreferenceManager
import com.pi.androidbasehiltmvvm.core.platform.ProjectApplication
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class ApiRequestInterceptor @Inject constructor(
    private val networkController: NetworkController?,
    private val preferenceManager: PreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkController?.isConnected()!!) {
            val exception = NetworkUnavailableException()
            ProjectApplication.networkStatusObservable.postValue(exception)
            throw exception
        }

        val request = chain.request()

        val response = chain.proceed(request)

        return response.also {
            networkController.inspectResponse(it)
        }
    }

    private fun addHeaderToRequest(request: Request): Request {
        val newRequest = request.newBuilder()

        /** If you want to add Token or Header value   */
        
        return newRequest.build()
    }

    companion object {
        const val HEADER_AUTHONRIZATION = "Authorization"
        const val HEADER_COOKIE = "Cookie"
        const val HEADER_VALUE = "header_value"
    }
}
