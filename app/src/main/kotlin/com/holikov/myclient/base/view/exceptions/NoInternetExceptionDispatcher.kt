package com.holikov.myclient.base.view.exceptions

import android.util.Log
import com.holikov.base.base.view.fragment.exceptions.ExceptionDispatcher
import com.holikov.data.network.exceptions.model.NetworkDisableException
import com.holikov.data.network.exceptions.model.NetworkTimeoutException
import java.net.UnknownHostException


class NoInternetExceptionDispatcher(private val callback: () -> Unit) :
    ExceptionDispatcher {

    override fun dispatch(exception: Throwable) {
        when {
            isNetworkException(exception) -> callback()
            else -> Log.e("ExceptionDispatcher",exception.message ?: "Invocation failed", exception)
        }
    }

    private fun isNetworkException(throwable: Throwable) =
        throwable is NetworkTimeoutException || throwable is UnknownHostException || throwable is NetworkDisableException
}