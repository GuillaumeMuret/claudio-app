package com.niji.claudio.common.tool

import io.ktor.client.engine.java.Java

actual object NetworkUtils {
    actual val defaultPlatformEngine = Java.create()
}