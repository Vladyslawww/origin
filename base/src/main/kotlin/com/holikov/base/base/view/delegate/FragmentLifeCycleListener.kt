package com.holikov.base.base.view.delegate

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment

interface FragmentLifeCycleListener {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    fun preCreateView(fragment: Fragment) {}
    fun postCreateView(fragment: Fragment, view: View) {}

    fun preDestroyView(fragment: Fragment, view: View) {}
    fun postDestroyView(fragment: Fragment) {}
}