package com.niji.claudio.common.tool

import io.ktor.client.engine.android.Android

actual object NetworkUtils {
    actual val defaultPlatformEngine = Android.create()
}