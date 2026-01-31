package com.kaushalpanjee.compose.data.network

import com.kaushalpanjee.core.util.AppUtil.createErrorResponse
import com.kaushalpanjee.core.util.Resource
import com.utilize.core.domain.model.response.BaseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.io.IOException

/**
* Created by Rishi Porwal 
*/

inline fun <Api, Result> safeApiFlow(
    crossinline apiCall: suspend () -> Api,
    crossinline mapper: (Api) -> Result
): Flow<Resource<Result>> =
    flow<Resource<Result>> {
        val response = apiCall()
        val mappedData = mapper(response)
        emit(Resource.Success(mappedData))
    }
        .onStart {
            emit(Resource.Loading())
        }
        .catch { e ->
            emit(Resource.Error(mapToError(e)))
        }.flowOn(Dispatchers.IO)



fun mapToError(throwable: Throwable): BaseErrorResponse {
    return when (throwable) {
        is HttpException -> when (throwable.code()) {

            401 -> BaseErrorResponse(
                code = 401,
                message = "Session expired. Please login again.",
                success = false
            )

            500 -> BaseErrorResponse(
                code = 500,
                message = "Server error. Try again later.",
                success = false
            )

            else -> BaseErrorResponse(
                code = throwable.code(),
                message = "Something went wrong.",
                success = false
            )
        }

        is IOException -> BaseErrorResponse(
            code = -1,
            message = "No internet connection",
            success = false
        )

        else -> BaseErrorResponse(
            code = -1,
            message = throwable.message ?: "Unknown error",
            success = false
        )
    }
}
