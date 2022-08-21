package com.pi.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.HttpUrl
import okhttp3.Response
import java.io.IOException

private const val UNKNOWN = "Unknown HTTP error occurred."

class NetworkController(
    val context: Context
) {

    @SuppressLint("MissingPermission")
    fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun inspectResponse(response: Response) {
        val httpCallError: HttpCallError<*>? = null
        val url = response.request.url
        val statusCode = response.code

        /*try {
            if (response.code > 300) {
                when (response.code) {
                    400 -> { // Bad Request
                        response.body?.let { body ->
                            httpCallError =
                                HttpCallError(
                                    url,
                                    statusCode,
                                    parseResponseBody<BadRequestErrorResponse>(body)
                                )
                        }
                    }

                    401, 403, 404, 500 -> { // Forbidden, Server Error
                        response.body?.let { body ->
                            httpCallError = HttpCallError(
                                url,
                                statusCode,
                                parseResponseBody<ErrorResponse>(body)
                            )
                        }
                    }

                    else -> {
                        httpCallError = HttpCallError(url, statusCode, UNKNOWN)
                    }
                }
            }
        } catch (e: Exception) {
            httpCallError = HttpCallError(url, statusCode, e)
        }*/

        /*httpCallError?.let {
            httpCallErrorObservable.onNext(it)
        }*/
    }

   /* companion object {
        val httpCallErrorObservable: PublishSubject<HttpCallError<*>> by lazy {
            PublishSubject.create()
        }
    }*/
}

class HttpCallError<T>(
    val url: HttpUrl,
    val statusCode: Int,
    val errorBody: T,
)


class HttpCallErrorException(val httpCallError: HttpCallError<*>) : RuntimeException()
class NetworkUnavailableException : IOException("Network connectivity could not established.")
class ServiceUnavailableException : IOException("Service unavailable.")
class NetworkConnectTimeoutException : IOException("Network connection timeout.")
class Authorization : IOException("Authorization.")
