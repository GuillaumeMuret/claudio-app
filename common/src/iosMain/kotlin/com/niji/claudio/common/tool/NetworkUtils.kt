package com.niji.claudio.common.tool

import io.ktor.client.engine.darwin.Darwin

actual object NetworkUtils {
    actual val defaultPlatformEngine = Darwin.create()
}