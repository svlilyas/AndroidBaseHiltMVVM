package com.pi.data.mapper

import com.pi.data.remote.ErrorResponse
import com.skydoves.sandwich.ApiErrorModelMapper
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message

/**
 * A mapper for mapping [ApiResponse.Failure.Error] response as custom [PokemonErrorResponse] instance.
 *
 * @see [ApiErrorModelMapper](https://github.com/skydoves/sandwich#apierrormodelmapper)
 */
object ErrorResponseMapper : ApiErrorModelMapper<ErrorResponse> {

    /**
     * maps the [ApiResponse.Failure.Error] to the [PokemonErrorResponse] using the mapper.
     *
     * @param apiErrorResponse The [ApiResponse.Failure.Error] error response from the network request.
     * @return A customized [PokemonErrorResponse] error response.
     */
    override fun map(apiErrorResponse: ApiResponse.Failure.Error<*>): ErrorResponse {
        return ErrorResponse(apiErrorResponse.statusCode.code, apiErrorResponse.message())
    }
}
