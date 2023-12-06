package com.niji.claudio.common.internal.repo.save

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.niji.claudio.common.tool.FileUtils
import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.data.save.IClaudioDatabase
import com.niji.claudio.common.tool.LogUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

actual class ClaudioDatabase : IClaudioDatabase {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private fun getDevicesFile() = File(getFileDir(), User.FILENAME_DEVICES)

    private fun getUserFile() = File(getFileDir(), User.FILENAME_USER)

    private fun getMediasFile() = File(getFileDir(), User.FILENAME_MEDIAS)

    private fun getDownloadedMediasFile() =File(getFileDir(), User.FILENAME_DOWNLOADED_MEDIAS)

    private fun getFavoriteMediasFile() = File(getFileDir(), User.FILENAME_FAVORITE)

    private fun getDataLogFile() = File(getFileDir(), User.FILENAME_DATALOG)

    private fun createDir(directory: File): File {
        if (!directory.exists()) directory.mkdir()
        if (!directory.exists()) {
            LogUtils.e(TAG, "Cannot create directory :'( check permissions and manifest -> ${directory.path}")
        }
        return directory
    }

    private fun getFileDir(): File {
        return createDir(File(FileUtils.getRootDirPath()))
    }

    actual fun getMediasDirectoryPath(): String {
        return createDir(File(FileUtils.getMediasDirectoryPath())).path
    }

    actual override suspend fun getDevices(): List<Device> {
        return try {
            gson.fromJson(
                getDevicesFile().readText(),
                object : TypeToken<List<Device>>() {})
        } catch (e: IOException) {
            LogUtils.d(TAG, "getDevices $e")
            listOf()
        }
    }

    actual override suspend fun getMedias(): List<Media> {
        return try {
            gson.fromJson(
                getMediasFile().readText(),
                object : TypeToken<List<Media>>() {})
        } catch (e: IOException) {
            LogUtils.d(TAG, "getMedias $e")
            listOf()
        }
    }

    actual override suspend fun getDownloadedMedias(): MutableList<Media> {
        return try {
            gson.fromJson(
                getDownloadedMediasFile().readText(),
                object : TypeToken<MutableList<Media>>() {})
        } catch (e: IOException) {
            LogUtils.d(TAG, "getDownloadedMedias $e")
            mutableListOf()
        }
    }

    actual override suspend fun getFavoriteMedias(): MutableList<Media> {
        return try {
            gson.fromJson(
                getFavoriteMediasFile().readText(),
                object : TypeToken<MutableList<Media>>() {})
        } catch (e: IOException) {
            LogUtils.d(TAG, "getFavoriteMedias $e")
            mutableListOf()
        }
    }

    actual override suspend fun setFavoriteMedias(medias: List<Media>) {
        try {
            getFavoriteMediasFile().writeText(gson.toJson(medias))
        } catch (e: IOException) {
            LogUtils.e(TAG, "setFavoriteMedias $e")
        }
    }

    actual override suspend fun saveDevices(devices: List<Device>) {
        try {
            getDevicesFile().writeText(gson.toJson(devices))
        } catch (e: IOException) {
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
            getMediasFile().writeText(gson.toJson(medias))
        } catch (e: IOException) {
            LogUtils.e(TAG, "saveMedias $e")
        }
    }

    actual suspend fun saveDownloadedMedias(downloadedMedias: List<Media>) {
        try {
            getDownloadedMediasFile().writeText(gson.toJson(downloadedMedias))
        } catch (e: IOException) {
            LogUtils.e(TAG, "saveDownloadedMedias $e")
        }
    }

    actual override suspend fun saveDownloadedMedia(downloadedMedia: Media) {
        try {
            val downloadedMedias = getDownloadedMedias()
            if (downloadedMedias.find { it.serverId == downloadedMedia.serverId } == null) {
                downloadedMedias.add(downloadedMedia)
            }
            getDownloadedMediasFile().writeText(gson.toJson(downloadedMedias))
        } catch (e: IOException) {
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
            gson.fromJson(getUserFile().readText(), User::class.java)
        } catch (e: JsonSyntaxException) {
            LogUtils.d(TAG, "getUser JsonSyntaxException $e")
            addUser(User())
        } catch (e: FileNotFoundException) {
            LogUtils.d(TAG, "getUser FileNotFoundException $e")
            addUser(User())
        }
    }

    actual override suspend fun addUser(user: User): User {
        try {
            getUserFile().writeText(gson.toJson(user))
        } catch (e: IOException) {
            LogUtils.e(TAG, "addUser $e")
        }
        return user
    }

    actual override suspend fun updateUser(user: User) {
        addUser(user)
    }

    actual override suspend fun getDataLogs(): List<DataLog> {
        return try {
            gson.fromJson(
                getDataLogFile().readText(),
                object : TypeToken<List<DataLog>>() {})
        } catch (e: JsonSyntaxException) {
            LogUtils.d(TAG, "getDataLogs JsonSyntaxException $e")
            emptyList()
        } catch (e: FileNotFoundException) {
            LogUtils.d(TAG, "getDataLogs FileNotFoundException $e")
            emptyList()
        }
    }

    actual override suspend fun addDataLog(dataLog: DataLog) {
        try {
            val list = getDataLogs().toMutableList()
            list.add(0, dataLog)
            while (list.size > 100) list.removeLast()
            getDataLogFile().writeText(gson.toJson(list))
        } catch (e: IOException) {
            LogUtils.e(TAG, "addDataLogs $e")
        }
    }

    companion object {
        private const val TAG = "ClaudioDatabase"
    }
}
