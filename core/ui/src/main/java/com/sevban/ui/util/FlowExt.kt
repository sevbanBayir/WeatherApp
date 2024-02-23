package com.sevban.ui.util

import com.sevban.network.ErrorType
import com.sevban.network.Failure
import com.sevban.network.util.ErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

val String.Companion.EMPTY: String by lazy { "" }

fun <T> Flow<T>.handleFailures(
    action: suspend (Failure) -> Unit
): Flow<T> =
    catch { error ->
        when (error) {
            is Failure -> action(error)
            else -> {
                val errorResponse = ErrorResponse(
                    message = error.message ?: String.EMPTY,
                    code = -2
                )
                val failure = Failure(errorType = ErrorType.UNEXPECTED_ERROR, errorResponse = errorResponse)
                action(failure)
            }
        }
    }