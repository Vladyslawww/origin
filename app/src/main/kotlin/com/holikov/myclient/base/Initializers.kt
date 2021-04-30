package com.holikov.myclient.base

import android.app.Application
import com.facebook.stetho.Stetho
import com.holikov.domain.AppInfoProvider
import timber.log.Timber

typealias Initializer = (app: Application) -> Unit

fun stethoInitializer(appInfo: AppInfoProvider): Initializer = {
    if (appInfo.isDebug) {
        Stetho.initializeWithDefaults(it)
    }
}

fun fabricInitializer(appInfo: AppInfoProvider): Initializer = {
    //if (!appInfo.isDebug) Fabric.with(it, Crashlytics())
}

fun timberInitializer(appInfo: AppInfoProvider): Initializer = {
    if (appInfo.isDebug) Timber.plant(Timber.DebugTree())
    //else Timber.plant(CrashReportingTree())
}

fun firebaseInitializer(): Initializer = {
    //FirebaseApp.InitializeApp(it)
}