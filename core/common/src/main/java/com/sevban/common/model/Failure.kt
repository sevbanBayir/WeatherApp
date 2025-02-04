package com.sevban.common.model

data class Failure(val errorType: ErrorType) : Exception()

enum class ErrorType {
    TOO_MANY_REQUESTS,
    SERVER_ERROR,
    UNKNOWN,
    NOT_FOUND,
    FORBIDDEN,
    UNAUTHORIZED,
    BAD_REQUEST,
    EMPTY_RESPONSE,
    SERIALIZATION,
    LOCATION_ERROR,
    CONNECTIVITY_ERROR
}