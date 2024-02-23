package com.sevban.network.util

import com.google.gson.GsonBuilder
import com.sevban.network.ErrorType
import com.sevban.network.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response

fun <T, Model> Flow<Response<T>>.asRestApiCall(mapper: (T) -> Model): Flow<Model> =
    map { response ->
        response.run {

            if (isSuccessful) {
                body() ?: throw Failure(ErrorType.EMPTY_RESPONSE, errorResponse = null)
            } else {
                val gson = GsonBuilder().create()
                val errorResponse = gson.fromJson(errorBody()?.string(), ErrorResponse::class.java)

                throw Failure(errorType = ErrorType.API_ERROR, errorResponse = errorResponse)
            }
            mapper(response.body()!!)
        }
    }.flowOn(Dispatchers.IO)

