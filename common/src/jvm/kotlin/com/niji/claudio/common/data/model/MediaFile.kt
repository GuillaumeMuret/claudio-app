package com.niji.claudio.common.data.model

import com.niji.claudio.common.tool.LogUtils
import java.io.File

actual class MediaFile(filePath: String) {

    private var mFile: File

    init {
        mFile = File(filePath)
        initParent(mFile)
    }

    actual fun appendBytes(bytes: ByteArray) {
        mFile.appendBytes(bytes)
    }

    actual fun getLength(): Long {
        return mFile.length()
    }

    actual fun getPath() = mFile.path ?: ""

    private fun initParent(file: File) {
        file.parent?.let { parent ->
            val parentFile = File(parent)
            while (!parentFile.exists()) {
                createDir(parentFile)
            }
        }
    }

    private fun createDir(directory: File): File {
        if (!directory.exists()) directory.mkdir()
        if (!directory.exists()) {
            LogUtils.d(TAG, "Cannot create dir -> ${directory.path}")
            LogUtils.d(TAG, "Try create parent -> ${directory.parent}")
            createDir(File(directory.parent))
        } else {
            LogUtils.d(TAG, "Directory created -> ${directory.path}")
        }
        return directory
    }

    companion object {
        private const val TAG = "MediaFile"
    }
}
