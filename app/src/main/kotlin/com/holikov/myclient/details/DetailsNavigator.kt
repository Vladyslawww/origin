package com.holikov.myclient.details

import androidx.fragment.app.FragmentManager
import com.holikov.myclient.base.navigator.BaseNavigator
import java.util.concurrent.atomic.AtomicReference

interface DetailsNavigator: BaseNavigator

internal class DetailsNavigatorImpl: DetailsNavigator {
    override val manager = AtomicReference<FragmentManager>()
}
