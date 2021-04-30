package com.holikov.myclient.base.navigator

import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.Gravity
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.holikov.base.base.navigator.ParentNavigator
import com.holikov.base.extensions.launchOnUi
import com.holikov.myclient.R
import java.util.concurrent.atomic.AtomicReference

interface BaseNavigator : ParentNavigator {

    val manager: AtomicReference<FragmentManager>

    fun attach(fragmentManager: FragmentManager) = manager.set(fragmentManager)

    fun release() = manager.set(null)

    fun FragmentManager.hasBackStack() = isRoot().not()

    fun FragmentManager.isRoot() = backStackEntryCount == 1

    fun FragmentManager.isContextAlive() = isDestroyed

    fun FragmentManager.go(@IdRes id: Int = R .id.container, fragment: Fragment, animate: Boolean = true) =
        launchOnUi { transaction(id, fragment, animate).commit() }

    fun FragmentManager.to(@IdRes id: Int = R.id.container, fragment: Fragment, animate: Boolean = true) =
        launchOnUi { transaction(id, fragment, animate).addToBackStack(fragment.javaClass.name).commit() }

    fun FragmentManager.shared(@IdRes id: Int = R.id.container, shared: View, fragment: Fragment) =
        launchOnUi { animateShared(id, shared, fragment).addToBackStack(fragment.javaClass.name).commit() }

    fun FragmentManager.pop() = launchOnUi { popBackStack() }

    private fun FragmentManager.transaction(@IdRes id: Int, fragment: Fragment, animate: Boolean = true) = when {
        animate -> animateTransaction(id, fragment)
        animate.not() -> defaultTransaction(id, fragment)
        else -> throw IllegalArgumentException("ignore")
    }

    private fun FragmentManager.defaultTransaction(@IdRes id: Int, fragment: Fragment) = beginTransaction().replace(id, fragment, fragment.javaClass.name)

    private fun FragmentManager.animateTransaction(@IdRes id: Int, fragment: Fragment) = fragment.apply {
        enterTransition = androidx.transition.Slide(Gravity.END)
            .apply { duration = TRANSACTION_DURATION }
        exitTransition = androidx.transition.Slide(Gravity.START)
            .apply { duration = TRANSACTION_DURATION }
    }.run { defaultTransaction(id, this) }

    private fun FragmentManager.animateShared(@IdRes id: Int, shared: View, fragment: Fragment) = fragment.apply {
        sharedElementEnterTransition = TransitionSet().apply {
            addTransition(TransitionInflater.from(shared.context).inflateTransition(android.R.transition.move))
            duration = MOVE_DEFAULT_TIME
            startDelay = TRANSACTION_DURATION
        }
        enterTransition = Fade().apply { startDelay = MOVE_DEFAULT_TIME + TRANSACTION_DURATION; duration = TRANSACTION_DURATION }
        exitTransition = Slide(Gravity.START).apply { duration = TRANSACTION_DURATION }
    }.run { defaultTransaction(id, this).addSharedElement(shared, shared.transitionName) }
}

private const val TRANSACTION_DURATION = 200L
private const val MOVE_DEFAULT_TIME = 800L