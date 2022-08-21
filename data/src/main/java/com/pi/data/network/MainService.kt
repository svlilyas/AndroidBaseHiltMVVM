package com.pi.data.network

import com.pi.data.remote.response.SampleResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface MainService {
    object Endpoint {
        const val mainPath = "/Sample"

        object Market {
            const val samplePath = "${mainPath}/products"
        }
    }

    @GET(Endpoint.Market.samplePath)
    suspend fun fetchPokemonInfo(name: String): ApiResponse<SampleResponse>
}
