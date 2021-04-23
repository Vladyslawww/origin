package com.holikov.base.di

import android.text.Editable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KodeinViewModelProvider private constructor() {

    companion object {
        inline fun <reified T : ViewModel> of(fragment: Fragment, crossinline factory: () -> T): T {

            @Suppress("UNCHECKED_CAST")
            val vmFactory = object : ViewModelProvider.Factory {
                override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
            }

            return ViewModelProvider(fragment, vmFactory)[T::class.java]
        }

        inline fun <reified T : ViewModel> of(fragmentActivity: FragmentActivity, crossinline factory: () -> T): T {

            @Suppress("UNCHECKED_CAST")
            val vmFactory = object : ViewModelProvider.Factory {
                override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
            }

            return ViewModelProvider(fragmentActivity, vmFactory)[T::class.java]
        }
    }
}
