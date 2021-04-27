package com.holikov.base.base.navigator

import androidx.fragment.app.FragmentManager

interface ParentNavigator {

    fun FragmentManager.parent() = if (backStackEntryCount == 1) head() else this.fragments[fragments.size - 2].parentFragmentManager

    fun FragmentManager.head() = fragments.firstOrNull()?.parentFragmentManager

    fun FragmentManager.current() = fragments.lastOrNull()
}