package com.niji.claudio.common.tool

expect object LogUtils {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable)
}