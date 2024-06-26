package com.sevban.ui.util

import com.sevban.common.extensions.EMPTY
import com.sevban.common.model.ErrorResponse
import com.sevban.common.model.ErrorType
import com.sevban.common.model.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

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
                val failure = Failure(
                    errorType = ErrorType.UNEXPECTED_ERROR,
                    errorResponse = errorResponse
                )
                action(failure)
            }
        }
    }