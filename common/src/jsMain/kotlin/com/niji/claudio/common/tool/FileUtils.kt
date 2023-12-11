package com.niji.claudio.common.tool

import com.niji.claudio.common.data.model.MediaFile


actual object FileUtils {
    actual fun fileExist(filePath: String) = false
    actual fun createMediaFile(filePath: String) = MediaFile(filePath)

    actual fun getMediasDirectoryPath(): String {
        // TODO("Not yet implemented")
        return ""
    }

    actual suspend fun generateTls(): String {
        // TODO("Not yet implemented")
        return ""
    }
}