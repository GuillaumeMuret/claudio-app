package com.niji.claudio.common.internal.repo.save

import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.data.save.IClaudioDatabase
import com.niji.claudio.common.tool.LogUtils
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding

actual class ClaudioDatabase : IClaudioDatabase {

    actual override suspend fun getDevices(): List<Device> {
        return try {
            val content = readFile(User.DIRECTORY_NAME + "_" + User.FILENAME_DEVICES)
            Json.decodeFromString(content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "getDevices $e", e)
            listOf()
        }
    }

    actual override suspend fun getMedias(): List<Media> {
        return try {
            val content = readFile(User.DIRECTORY_NAME + "_" + User.FILENAME_MEDIAS)
            Json.decodeFromString(content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "getMedias $e", e)
            listOf()
        }
    }

    actual override suspend fun getDownloadedMedias(): MutableList<Media> {
        return try {
            val content = readFile(User.DIRECTORY_NAME + "_" + User.FILENAME_DOWNLOADED_MEDIAS)
            Json.decodeFromString(content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "getDownloadedMedias $e", e)
            mutableListOf()
        }
    }

    actual override suspend fun getFavoriteMedias(): MutableList<Media> {
        return try {
            val content = readFile(User.DIRECTORY_NAME + "_" + User.FILENAME_FAVORITE)
            Json.decodeFromString(content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "getFavoriteMedias $e", e)
            mutableListOf()
        }
    }

    actual override suspend fun setFavoriteMedias(medias: List<Media>) {
        try {
            val content = Json.encodeToString(medias)
            writeFile(User.DIRECTORY_NAME + "_" + User.FILENAME_FAVORITE, content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "setFavoriteMedias $e")
        }
    }

    actual override suspend fun saveDevices(devices: List<Device>) {
        try {
            val content = Json.encodeToString(devices)
            writeFile(User.DIRECTORY_NAME + "_" + User.FILENAME_DEVICES, content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "saveDevices $e")
        }
    }

    actual override suspend fun deleteDevice(device: Device) {
        // TODO("Not yet implemented")
    }

    actual override suspend fun deleteAllDevice() {
        // TODO("Not yet implemented")
    }

    actual override suspend fun saveMedias(medias: List<Media>) {
        try {
            val content = Json.encodeToString(medias)
            writeFile(User.DIRECTORY_NAME + "_" + User.FILENAME_MEDIAS, content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "saveMedias $e")
        }
    }

    actual suspend fun saveDownloadedMedias(downloadedMedias: List<Media>) {
        try {
            val content = Json.encodeToString(downloadedMedias)
            writeFile(User.DIRECTORY_NAME + "_" + User.FILENAME_DOWNLOADED_MEDIAS, content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "saveDownloadedMedias $e")
        }
    }

    actual override suspend fun saveDownloadedMedia(downloadedMedia: Media) {
        try {
            val downloadedMedias = getDownloadedMedias()
            if (downloadedMedias.find { it.serverId == downloadedMedia.serverId } == null) {
                downloadedMedias.add(downloadedMedia)
            }
            saveDownloadedMedias(downloadedMedias)
        } catch (e: Exception) {
            LogUtils.e(TAG, "saveDownloadedMedia $e")
        }
    }

    actual override suspend fun deleteMedia(media: Media) {
        val downloadedMedias = getDownloadedMedias()
        val mediaToRemove = downloadedMedias.find { it.serverId == media.serverId }
        downloadedMedias.remove(mediaToRemove)
        saveDownloadedMedias(downloadedMedias)
        media.isDownloadedState = false
    }

    actual override suspend fun getUser(): User {
        return try {
            val content = readFile(User.DIRECTORY_NAME + "_" + User.FILENAME_USER)
            Json.decodeFromString(content)
        } catch (e: Exception) {
            LogUtils.d(TAG, "getUser Exception $e")
            addUser(User())
        }
    }

    actual override suspend fun addUser(user: User): User {
        try {
            val content = Json.encodeToString(user)
            writeFile(User.DIRECTORY_NAME + "_" + User.FILENAME_USER, content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "addUser $e")
        }
        return user
    }

    actual override suspend fun updateUser(user: User) {
        addUser(user)
    }

    actual override suspend fun getDataLogs(): List<DataLog> {
        return try {
            val content = readFile(User.DIRECTORY_NAME + "_" + User.FILENAME_DATALOG)
            Json.decodeFromString(content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "getDataLogs $e")
            emptyList()
        }
    }

    actual override suspend fun addDataLog(dataLog: DataLog) {
        try {
            val list = getDataLogs().toMutableList()
            list.add(0, dataLog)
            while (list.size > 100) list.removeLast()
            val content = Json.encodeToString(list)
            writeFile(User.DIRECTORY_NAME + "_" + User.FILENAME_DATALOG, content)
        } catch (e: Exception) {
            LogUtils.e(TAG, "addDataLogs $e")
        }
    }

    actual fun getMediasDirectoryPath(): String {
        val fileManager = NSFileManager.defaultManager
        val fileUrl = (fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .last() as? NSURL)
        return fileUrl?.path ?: ""
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun writeFile(filename: String, content: String) {
        val fileManager = NSFileManager.defaultManager
        val fileUrl = (fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .last() as? NSURL)?.URLByAppendingPathComponent(filename)
        LogUtils.d(TAG, "write file -> $fileUrl")
        val nsContent: NSData? = memScoped {
            val string = NSString.create(string = content)
            string.dataUsingEncoding(NSUTF8StringEncoding)
        }
        memScoped {
            fileUrl?.path?.let { safePath ->
                content.usePinned {
                    fileManager.createFileAtPath(safePath, nsContent, null)
                }
            }
        }
    }

    private fun readFile(filename: String): String {
        val fileManager = NSFileManager.defaultManager
        val fileUrl = (fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .last() as? NSURL)?.URLByAppendingPathComponent(filename)
        val nsContent = fileManager.contentsAtPath(fileUrl?.path ?: "")
        val content = nsContent?.let { NSString.create(it, NSUTF8StringEncoding) } as? String
        return content ?: ""
    }

    companion object {
        private const val TAG = "ClaudioDatabase"
    }
}