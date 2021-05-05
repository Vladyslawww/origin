package com.holikov.myclient.base.view


import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.holikov.base.base.livedata.ConnectionLiveData
import com.holikov.base.base.view.activity.ConnectionLostHandler
import com.holikov.base.base.view.activity.InjectionActivity
import com.holikov.base.extensions.isEmpty
import com.holikov.myclient.base.navigator.BaseNavigator
import com.holikov.myclient.base.view.exceptions.NoInternetExceptionDispatcher
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.holikov.myclient.R
import com.holikov.myclient.base.extentions.longToast
import com.holikov.myclient.base.extentions.toast
import java.util.concurrent.Executor

abstract class BaseActivity : InjectionActivity(), BaseNavigator, ConnectionLostHandler {

    @get:LayoutRes
    protected abstract val layoutResId: Int
    protected abstract val isMainRoot: Boolean

    override val manager = AtomicReference<FragmentManager>()

    val exceptionDispatcher = NoInternetExceptionDispatcher(::onConnectionLost)

    lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        connectionLiveData = ConnectionLiveData(this)
        attach(supportFragmentManager)
        initSession()

        Timber.v("onCreate ${javaClass.simpleName}")
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    abstract fun start()

    private fun initSession() {
        /*if (manager.get().isEmpty()) {
            if (isMainRoot) checkBioAuth()
        } else*/ start()
    }

    private fun checkBioAuth() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> authUser(executor)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> longToast(R.string.error_msg_no_biometric_hardware)
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> longToast(R.string.error_msg_biometric_hw_unavailable)
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> longToast(R.string.error_msg_biometric_not_setup)
        }
    }

    private fun authUser(executor: Executor) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(getString(R.string.auth_title)).setSubtitle(getString(R.string.auth_subtitle))
            setDescription(getString(R.string.auth_description)).setDeviceCredentialAllowed(true)
        }
        BiometricPrompt(this, executor, authCallback).run { authenticate(promptInfo.build()) }
    }

    private val authCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result); start()
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(
                errorCode,
                errString
            ); toast(getString(R.string.error_msg_auth_error, errString))
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed(); toast(R.string.error_msg_auth_failed)
        }
    }
}