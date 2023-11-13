package com.niji.claudio.common.data.api.exception

class ErrorRequestException : Exception() {
    override val message: String
        get() = "Error code received :("
}