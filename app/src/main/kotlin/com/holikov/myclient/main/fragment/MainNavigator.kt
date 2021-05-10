package com.holikov.myclient.main.fragment

import androidx.fragment.app.FragmentManager
import com.holikov.myclient.base.navigator.BaseNavigator
import java.util.concurrent.atomic.AtomicReference

interface MainNavigator: BaseNavigator

internal class MainNavigatorImpl: MainNavigator {
    override val manager = AtomicReference<FragmentManager>()

}