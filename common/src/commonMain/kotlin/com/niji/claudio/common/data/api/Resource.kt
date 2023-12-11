package com.niji.claudio.common.data.api

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    class Success<out T>(
        val data: T?,
        val source: DataSource = DataSource.NETWORK,
        val isOnline: Boolean = true
    ) : Resource<T>()

    class Error(val exception: Exception, val code: Int? = null) : Resource<Nothing>()
}

enum class DataSource {
    NETWORK,
    CACHE,
}