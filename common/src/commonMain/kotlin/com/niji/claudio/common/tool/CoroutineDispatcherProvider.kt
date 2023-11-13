package com.niji.claudio.common.tool

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher


expect object CoroutineDispatcherProvider {
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun ui(): MainCoroutineDispatcher
}