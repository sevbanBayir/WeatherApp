package com.sevban.network

import com.sevban.network.util.ErrorResponse

data class Failure(
    val errorType: ErrorType,
    val errorResponse: ErrorResponse?
) : Exception(errorResponse?.message)

enum class ErrorType {
    API_ERROR,
    UNEXPECTED_ERROR,
    TIMEOUT_ERROR,
    NO_CONNECTION_ERROR,
    EMPTY_RESPONSE,
}