package com.pi.data.repository

import androidx.annotation.WorkerThread
import com.pi.data.mapper.ErrorResponseMapper
import com.pi.data.network.MainClient
import com.pi.data.persistence.AppDao
import com.skydoves.pokedex.repository.Repository
import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainClient: MainClient,
    private val appDao: AppDao,
    private val ioDispatcher: CoroutineDispatcher
) : Repository {

    @WorkerThread
    fun fetchSampleInfo(
        name: String,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val sampleInfo = appDao.getSampleInfo(name)
        if (sampleInfo == null) {
            /**
             * fetches a [SampleResponse] from the network and getting [ApiResponse] asynchronously.
             * @see [suspendOnSuccess](https://github.com/skydoves/sandwich#apiresponse-extensions-for-coroutines)
             */
            val response = mainClient.fetchSampleInfo(name = name)
            response.suspendOnSuccess {
                appDao.insert(data)
                emit(data)
            }
                // handles the case when the API request gets an error response.
                // e.g., internal server error.
                .onError {
                    /** maps the [ApiResponse.Failure.Error] to the [SampleErrorResponse] using the mapper. */
                    map(ErrorResponseMapper) { onError("[Code: $code]: $message") }
                }
                // handles the case when the API request gets an exception response.
                // e.g., network connection error.
                .onException { onError(message) }
        } else {
            emit(sampleInfo)
        }
    }.onCompletion { onComplete() }.flowOn(ioDispatcher)
}
