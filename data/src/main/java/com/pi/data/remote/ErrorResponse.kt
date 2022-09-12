package com.pi.data.remote

/**
 * @param code A network response code.
 * @param message A network error message.
 */
data class ErrorResponse(
    val code: Int,
    val message: String?
)
