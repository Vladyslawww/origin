package com.holikov.myclient.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

interface ForegroundAppDetector {
    val isAppInForeground: Boolean
}

class ForegroundAppDetectorImpl : LifecycleObserver, ForegroundAppDetector {

    override var isAppInForeground: Boolean = false
        private set

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        isAppInForeground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        isAppInForeground = false
    }
}
