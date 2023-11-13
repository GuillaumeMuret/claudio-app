package com.niji.claudio.common.data.api.exception

class OfflineException : Exception() {
    override val message: String
        get() = "You are offline"
}