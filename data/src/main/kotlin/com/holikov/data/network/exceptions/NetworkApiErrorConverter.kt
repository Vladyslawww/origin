package com.holikov.data.network.exceptions

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

interface NetworkApiErrorConverter {

    fun <T> convert(clazz: Class<T>, errorBody: ResponseBody): T?
}

internal class NetworkApiErrorConverterImpl(private val retrofit: Retrofit): NetworkApiErrorConverter {

    override fun <T> convert(clazz: Class<T>, errorBody: ResponseBody): T? {
        val converter: Converter<ResponseBody, T> = retrofit.responseBodyConverter(clazz, emptyArray())
        return converter.convert(errorBody)
    }

}