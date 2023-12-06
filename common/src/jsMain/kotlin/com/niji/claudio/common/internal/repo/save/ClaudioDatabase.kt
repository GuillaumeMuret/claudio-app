package com.niji.claudio.common.internal.repo.save

import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.data.save.IClaudioDatabase


actual class ClaudioDatabase : IClaudioDatabase {

    var mMedias: List<Media> = emptyList()
    var mDevices: List<Device> = emptyList()

    var currentUser: User? = null

    actual override suspend fun getDevices(): List<Device> {
        // TODO not yet implemented
        return mDevices
    }

    actual override suspend fun getMedias(): List<Media> {
        // TODO not yet implemented
        return mMedias
        // return try {
        //     gson.fromJson(
        //         getMediasFile().readText(),
        //         object : TypeToken<List<Media>>() {})
        // } catch (e: IOException) {
        //     LogUtils.d(TAG, "getMedias $e")
        //     listOf()
        // }
    }

    actual override suspend fun getDownloadedMedias(): MutableList<Media> {
        // TODO not yet implemented
        return mutableListOf()
    }

    actual override suspend fun getFavoriteMedias(): MutableList<Media> {
        // TODO not yet implemented
        return mutableListOf()
    }

    actual override suspend fun setFavoriteMedias(medias: List<Media>) {
        // TODO not yet implemented
    }

    actual override suspend fun saveDevices(devices: List<Device>) {
        // TODO not yet implemented
        mDevices = devices
    }

    actual override suspend fun deleteDevice(device: Device) {
        // TODO("Not yet implemented")
    }

    actual override suspend fun deleteAllDevice() {
        // TODO("Not yet implemented")
    }

    actual override suspend fun saveMedias(medias: List<Media>) {
        // TODO not yet implemented
        mMedias = medias
    }

    actual override suspend fun saveDownloadedMedia(downloadedMedia: Media) {
        // TODO not yet implemented
    }

    actual override suspend fun deleteMedia(media: Media) {
        // TODO not yet implemented
    }

    actual override suspend fun getUser(): User {
        // TODO not yet implemented
        if (currentUser == null) {
            currentUser = addUser(User(name = "Browser"))
        }
        return currentUser ?: User()
    }

    actual override suspend fun addUser(user: User): User {
        // TODO not yet implemented
        currentUser = user
        return getUser()
    }

    actual override suspend fun updateUser(user: User) {
        // TODO not yet implemented
        currentUser = user
    }

    actual override suspend fun getDataLogs(): List<DataLog> {
        // TODO not yet implemented
        return mutableListOf()
    }

    actual override suspend fun addDataLog(dataLog: DataLog) {
        // TODO not yet implemented
    }

    actual suspend fun saveDownloadedMedias(downloadedMedias: List<Media>) {
        // TODO not yet implemented
    }

    actual fun getMediasDirectoryPath(): String {
        // TODO not yet implemented
        return ""
    }
}
