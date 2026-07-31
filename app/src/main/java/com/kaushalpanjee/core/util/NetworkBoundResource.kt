package com.kaushalpanjee.core.util

import com.google.gson.Gson
import com.utilize.core.domain.model.response.BaseErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.HttpURLConnection

fun <ResultType, RequestType> networkBoundResource(
    query: () -> Flow<ResultType>,
    fetch: suspend () -> RequestType,
    saveFetchResult: suspend (RequestType) -> Unit,
    shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    val data = query().first()
    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        try {
            saveFetchResult(fetch())
            query().map {
                Resource.Success(it)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            val error = if (t is HttpException)
                getErrorMessage(t)
            else
                BaseErrorResponse(0, "Something went wrong !!", false, Any())
            query().map { Resource.Error(error, it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)

}



fun <RequestType> networkBoundResourceWithoutDb(
    fetch: suspend () -> RequestType
): Flow<Resource<RequestType>> = flow {

    emit(Resource.Loading(null))

    try {
        emit(Resource.Success(fetch.invoke()))
    } catch (t: Throwable) {

        val error = if (t is HttpException) {
            getErrorMessage(t)
        } else {
            if (t.message?.contains("Unable to resolve host", ignoreCase = true) == true) {
                BaseErrorResponse(
                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
                    "No Internet Connection",
                    false,
                    Any()
                )
            } else {
                BaseErrorResponse(
                    0,
                    t.message ?: "Something went wrong",
                    false,
                    Any()
                )
            }
        }

        emit(Resource.Error(error, null))
    }
}
//fun <RequestType> networkBoundResourceWithoutDb(
//    fetch: suspend () -> RequestType
//) = flow {
//
//    emit(Resource.Loading(null))
//    try {
//        emit(Resource.Success(fetch.invoke()))
//    } catch (t: Throwable) {
//        val error = if (t is HttpException)
//            getErrorMessage(t)
//        else{
//            if (t.message!!.contains("Unable to resolve host")){
//                 BaseErrorResponse(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "No Internet Connection", false,Any())
//            }else BaseErrorResponse(0, t.message.toString(), false, Any())
//        }
//
//        emit(Resource.Error(error, null))
//    }
//}








fun <T> networkBoundResourceWithoutDbn(
    apiCall: suspend () -> T
): Flow<Resource<T>> {
    return flow {
        emit(Resource.Loading())

        val response = apiCall()
        emit(Resource.Success(response))
    }.catch { e ->
        emit(
            Resource.Error(
                BaseErrorResponse(
                    code = 0,
                    message = e.message ?: "Unknown error",
                    success = false,
                    data = Any()
                )
            )
        )
    }
}


fun getErrorMessage(throwable: HttpException): BaseErrorResponse {

    if (throwable.code() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
        return BaseErrorResponse(
            HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
            "No Internet Connection",
            false,
            Any()
        )
    }

    val errorBody = throwable.response()?.errorBody()

    if (errorBody == null) {
        return BaseErrorResponse(
            throwable.code(),
            throwable.message(),
            false,
            Any()
        )
    }

    return try {

        val body = errorBody.string()

        //Log.e("API_ERROR", body)

        Gson().fromJson(body, BaseErrorResponse::class.java)
            ?: BaseErrorResponse(
                throwable.code(),
                throwable.message(),
                false,
                Any()
            )

    } catch (e: Exception) {

        //Log.e("API_ERROR", e.message ?: "")

        BaseErrorResponse(
            throwable.code(),
            throwable.message(),
            false,
            Any()
        )
    }
}