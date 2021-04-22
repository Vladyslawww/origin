package com.holikov.domain

interface AppInfoProvider {
    val androidId: String
    val deviceName: String
    val isDebug: Boolean
    val applicationId: String
    val buildType: String
    val versionCode: Int
    val versionName: String
    val osVersion: String
    val endpoint: String
    val apiKey: String
    val appName: String
}