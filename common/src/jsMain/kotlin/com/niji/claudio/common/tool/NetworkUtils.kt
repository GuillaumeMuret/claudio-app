package com.niji.claudio.common.tool

import io.ktor.client.engine.js.Js

actual object NetworkUtils {
    actual val defaultPlatformEngine = Js.create()
}