package com.niji.claudio.common.tool

actual object LogUtils {
    actual fun d(tag: String, message: String) {
        console.log("$tag - debug - $message")
    }

    actual fun e(tag: String, message: String) {
        console.log("$tag - error - $message")
    }

    actual fun e(tag: String, message: String, throwable: Throwable) {
        console.log("$tag - error - $message - $throwable")
    }
}