package com.niji.claudio.common.data.model

import java.io.File

actual class MediaFile(filePath: String) {

    private var mFile: File? = null

    init {
        mFile = File(filePath)
    }

    actual fun appendBytes(bytes: ByteArray) {
        mFile?.appendBytes(bytes)
    }

    actual fun getLength(): Long {
        return mFile?.length() ?: 0
    }

    actual fun getPath() = mFile?.path ?: ""
}
