package com.holikov.myclient.splash

import androidx.fragment.app.FragmentManager
import com.holikov.myclient.base.navigator.BaseNavigator
import com.holikov.myclient.main.fragment.MainFragment
import java.util.concurrent.atomic.AtomicReference

interface SplashNavigator : BaseNavigator {
    fun signIn() {}
    fun main() = manager.get()?.go(fragment = MainFragment())
}

internal class SplashNavigatorImpl : SplashNavigator {
    override val manager = AtomicReference<FragmentManager>()
}