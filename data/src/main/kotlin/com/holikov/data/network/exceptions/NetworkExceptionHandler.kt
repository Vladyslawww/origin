package com.holikov.data.network.exceptions

import com.holikov.data.network.exceptions.model.*
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.UnknownHostException
import com.holikov.base.mappers.mapItem

interface ExceptionHandler {

    fun propagate(throwable: Throwable): Throwable
}

class NetworkExceptionHandler(private val networkErrorConverter: NetworkApiErrorConverter): ExceptionHandler {


    override fun propagate(throwable: Throwable) = when {
        throwable is NetworkException -> throwable
        isNetworkDisabledException(throwable) -> NetworkDisableException(throwable)
        isUnauthorizedException(throwable) -> NotAuthorizedException(throwable)
        isNetworkUnprocessableException(throwable) -> createUnprocessableNetworkException(throwable as HttpException)
        isInternalApiException(throwable) -> NetworkInternalException(throwable)
        isNotFoundApiException(throwable) -> NetworkNotFoundException(throwable)
        throwable is HttpException -> NetworkException(throwable)
        else -> throwable
    }

    private fun isNetworkDisabledException(throwable: Throwable): Boolean {
        return throwable is NetworkTimeoutException || throwable is UnknownHostException || throwable is NetworkDisableException
    }

    private fun isUnauthorizedException(throwable: Throwable): Boolean {
        return (throwable as? HttpException)?.code() == UnauthorizedError.CODE
    }

    private fun isNetworkUnprocessableException(throwable: Throwable): Boolean {
        return (throwable as? HttpException)?.code() == BadRequestError.CODE
    }

    private fun isInternalApiException(throwable: Throwable): Boolean {
        return (throwable as? HttpException)?.code() == InternalServerError.CODE
    }

    private fun isNotFoundApiException(throwable: Throwable): Boolean {
        return (throwable as? HttpException)?.code() == ValidationError.CODE
    }

    private fun createUnprocessableNetworkException(httpException: HttpException): NetworkUnprocessableException {
        val apiError = convertError(httpException)
        return NetworkUnprocessableException(httpException, apiError.mapItem(ApiError::toDomain))
    }

    private fun convertError(httpException: HttpException): ApiError {
        val apiError = tryOr { createApiError<ApiError>(httpException.response()?.errorBody()!!) }
        return apiError?: ApiError("Failed parse api error", emptyMap())
    }

    private inline fun <reified T> createApiError(responseBody: ResponseBody): T? {
        return networkErrorConverter.convert(T::class.java, responseBody)
    }
}

fun <T> tryOr(action: () -> T): T? {
    return try {
        action()
    } catch (ignore: Exception) {
        return null
    }
}