package com.niji.claudio.common.tool

import io.ktor.client.engine.HttpClientEngine

expect object NetworkUtils {
    val defaultPlatformEngine: HttpClientEngine
}