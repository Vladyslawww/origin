package com.holikov.myclient.splash

import android.os.Bundle
import android.view.View
import com.holikov.myclient.R
import com.holikov.myclient.base.extentions.gone
import com.holikov.myclient.base.view.BaseFragment
import kotlinx.android.synthetic.main.splash_fragment.*
import org.kodein.di.generic.instance

internal class SplashFragment : BaseFragment<StateSplash, SplashViewModel, SplashNavigator>() {

    override val fragmentModule = SplashModule()
    override val navigator by instance<SplashNavigator>()
    override val viewModel by instance<SplashViewModel>()
    override val layoutResourceId = R.layout.splash_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.toMain) { navigator.main() }
        observe(viewModel.toSignIn) { navigator.signIn() }
        viewModel.loadData()
    }

    override fun onStateChanged(state: StateSplash) {
        when {
            state.isLoading.not() -> splashView.apply { cancelAnimation(); gone() }
        }
    }
}