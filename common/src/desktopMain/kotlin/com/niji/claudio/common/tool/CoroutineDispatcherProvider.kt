package com.niji.claudio.common.tool

import kotlinx.coroutines.Dispatchers

actual object CoroutineDispatcherProvider {
    actual fun io() = Dispatchers.Unconfined
    actual fun default() = Dispatchers.Default
    actual fun ui() = Dispatchers.Main
}