package com.niji.claudio.common.tool

import android.util.Log

actual object LogUtils {
    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    actual fun e(tag: String, message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }
}