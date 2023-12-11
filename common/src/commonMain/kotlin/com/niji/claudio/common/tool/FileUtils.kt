package com.niji.claudio.common.tool

import com.niji.claudio.common.data.model.MediaFile

expect object FileUtils {
    fun fileExist(filePath: String): Boolean
    fun createMediaFile(filePath: String): MediaFile
    fun getMediasDirectoryPath(): String
    suspend fun generateTls(): String
}
