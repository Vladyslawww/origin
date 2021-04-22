package com.holikov.data.network

import com.holikov.data.network.exceptions.model.NetworkTimeoutException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

class TransportExceptionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        try {
            return chain.proceed(request)
        } catch (e: Throwable) {
            throw mapExceptions(e, request) as Throwable
        }
    }

    private fun mapExceptions(e: Throwable, request: Request) =
        when (e) {
            //ConnectException is caused by timeout and causes crashes if not mapped
            is SocketTimeoutException, is ConnectException -> NetworkTimeoutException(e, request)
            else -> e
        }
}