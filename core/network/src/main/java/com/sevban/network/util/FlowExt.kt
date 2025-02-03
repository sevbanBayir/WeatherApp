package com.sevban.network.util

import com.sevban.common.model.ErrorType
import com.sevban.common.model.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

fun <T, Model> Flow<Response<T>>.asRestApiCall(mapper: (T) -> Model): Flow<Model> =
    map { response ->
        if (response.isSuccessful.not()) {
            when (response.code()) {
                400 -> throw Failure(ErrorType.BAD_REQUEST)
                401 -> throw Failure(ErrorType.UNAUTHORIZED)
                403 -> throw Failure(ErrorType.FORBIDDEN)
                404 -> throw Failure(ErrorType.NOT_FOUND)
                429 -> throw Failure(ErrorType.TOO_MANY_REQUESTS)
                in 500..599 -> throw Failure(ErrorType.SERVER_ERROR)
                else -> throw Failure(ErrorType.UNKNOWN)
            }
        }

        val body = response.body() ?: throw Failure(ErrorType.EMPTY_RESPONSE)

        try {
            mapper(body)
        } catch (e: Exception) {
            throw Failure(ErrorType.SERIALIZATION)
        }
    }
