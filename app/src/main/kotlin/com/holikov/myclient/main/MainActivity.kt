package com.holikov.myclient.main

import com.holikov.myclient.R
import com.holikov.myclient.base.view.BaseActivity
import com.holikov.myclient.splash.SplashFragment

class MainActivity : BaseActivity() {
    override val isMainRoot = true
    override val layoutResId: Int get() = R.layout.activity_main
    override fun start() = manager.get().go(fragment = SplashFragment())
}