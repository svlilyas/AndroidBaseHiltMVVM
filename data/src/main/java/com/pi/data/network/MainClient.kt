package com.pi.data.network

import com.pi.data.remote.response.SampleResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class MainClient @Inject constructor(
    private val mainService: MainService
) {

    suspend fun fetchSampleInfo(
        name: String
    ): ApiResponse<SampleResponse> =
        mainService.fetchPokemonInfo(
            name = name
        )
}
