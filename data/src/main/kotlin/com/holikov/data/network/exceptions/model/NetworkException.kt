package com.holikov.data.network.exceptions.model

import com.squareup.moshi.Json

open class NetworkException(cause: Throwable) : RuntimeException(cause)

data class NetworkUnprocessableApiError(val message: String?, val fields: Map<String, List<NetworkApiErrorReason>>)

data class NetworkApiErrorReason(val error: String, val context: Map<String, Any?>?)

data class ApiError(
    @field:Json(name = "message") val message: String?,
    @field:Json(name = "fields") val fields: Map<String, List<ApiErrorReason>>?)

data class ApiErrorReason(
    @field:Json(name = "error") val error: String,
    @field:Json(name = "context") val context: Map<String, Any?>?)

fun ApiError.toDomain() =
    NetworkUnprocessableApiError(
        message = message,
        fields = fields?.mapValues { it.value.map { reason -> NetworkApiErrorReason(error = reason.error, context = reason.context ?: emptyMap()) } } ?: emptyMap()
    )