package com.holikov.base.base.view.fragment

interface StateHandler<in T> {
    fun onStateChanged(state: T)
}