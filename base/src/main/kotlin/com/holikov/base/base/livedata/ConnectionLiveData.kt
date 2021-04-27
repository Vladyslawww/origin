package com.holikov.base.base.livedata

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData

class ConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private var isConnected = false

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    init {
        isConnected = connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    override fun onActive() {
        super.onActive()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> nNetworkAvailableRequest()
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkAvailableRequest() {
        val builder = NetworkRequest.Builder()
            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)

        connectivityManager.registerNetworkCallback(builder.build(),
            getConnectivityManagerCallback())
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun nNetworkAvailableRequest() {
        connectivityManager.registerDefaultNetworkCallback(getConnectivityManagerCallback())
    }

    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network?) {
                    if (!isConnected) {
                        isConnected = true
                        postValue(true)
                    }
                }

                override fun onLost(network: Network?) {
                    if (isConnected) {
                        isConnected = false
                        postValue(false)
                    }
                }
            }
            return connectivityManagerCallback
        } else
            throw IllegalAccessError("SDK version has to be >= ${Build.VERSION_CODES.LOLLIPOP}")
    }
}
