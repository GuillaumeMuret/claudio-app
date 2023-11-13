package com.niji.claudio.common.data.model

actual class MediaFile(private val filePath: String) {

    actual fun appendBytes(bytes: ByteArray) {
        // TODO
    }

    actual fun getLength(): Long {
        // TODO
        return 0
    }

    actual fun getPath(): String {
        // TODO
        return ""
    }
}