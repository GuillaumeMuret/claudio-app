package com.niji.claudio.common.data.model

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect class MediaFile {
    fun appendBytes(bytes: ByteArray)
    fun getLength(): Long
    fun getPath(): String
}
