package com.niji.claudio.common.tool

expect object NotificationUtils {
    fun createNotification(title: String, content: String)
}