package com.niji.claudio.common.tool

actual object LogUtils {
    actual fun d(tag: String, message: String) {
        println("$tag - debug - $message")
    }

    actual fun e(tag: String, message: String) {
        println("$tag - error - $message")
    }

    actual fun e(tag: String, message: String, throwable: Throwable) {
        println("$tag - error - $message - $throwable")
    }
}