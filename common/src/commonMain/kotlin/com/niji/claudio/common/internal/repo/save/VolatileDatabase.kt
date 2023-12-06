package com.niji.claudio.common.internal.repo.save

import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.data.save.IClaudioDatabase
import com.niji.claudio.common.tool.LogUtils

internal class VolatileDatabase : IClaudioDatabase {

    // ############################################################################
    // Medias
    // ############################################################################

    private var cacheMedias: List<Media> = mutableListOf()

    override suspend fun getMedias() = cacheMedias

    override suspend fun saveMedias(medias: List<Media>) {
        cacheMedias = medias
    }

    override suspend fun setFavoriteMedias(medias: List<Media>) {
        cacheMedias.filter { media ->
            medias.find { favoriteMedia ->
                favoriteMedia.serverId == media.serverId
            } != null
        }.onEach { cacheMedia ->
            cacheMedia.isFavorite = medias.find { it.serverId == cacheMedia.serverId }?.isFavorite
        }
        saveMedias(cacheMedias)
    }

    override suspend fun saveDownloadedMedia(downloadedMedia: Media) {
        saveMedias(listOf(downloadedMedia))
    }

    override suspend fun deleteMedia(media: Media) {
        media.serverId?.let { deleteMediaByServerId(it) }
    }

    private fun deleteMediaByServerId(serverId: String) {
        cacheMedias.dropWhile { it.serverId == serverId }
    }

    override suspend fun getDownloadedMedias(): List<Media> {
        return cacheMedias.filter { it.isDownloaded == true }
    }

    override suspend fun getFavoriteMedias(): List<Media> {
        return cacheMedias.filter { it.isFavorite == true }
    }

    // ############################################################################
    // Devices
    // ############################################################################

    private var cacheDevices: List<Device> = mutableListOf()

    override suspend fun getDevices(): List<Device> {
        return cacheDevices
    }

    override suspend fun saveDevices(devices: List<Device>) {
        cacheDevices = devices
    }

    override suspend fun deleteDevice(device: Device) {
        cacheDevices.dropWhile { it.serverId == device.serverId }
    }

    override suspend fun deleteAllDevice() {
        cacheDevices = mutableListOf()
    }

    // ############################################################################
    // User
    // ############################################################################

    private var cacheUser: User? = null

    override suspend fun getUser(): User {
        return cacheUser ?: run {
            LogUtils.d(TAG, "No user found")
            addUser(User(name = "Volatile User"))
        }
    }

    override suspend fun addUser(user: User): User {
        upsertUser(user)
        return getUser()
    }

    override suspend fun updateUser(user: User) {
        upsertUser(user)
    }

    private fun upsertUser(user: User) {
        cacheUser = user
    }

    // ############################################################################
    // DataLog
    // ############################################################################

    private var cacheDataLogs: MutableList<DataLog> = mutableListOf()

    override suspend fun getDataLogs(): List<DataLog> {
        return cacheDataLogs
    }

    override suspend fun addDataLog(dataLog: DataLog) {
        cacheDataLogs.add(dataLog)
    }

    companion object {
        private const val TAG = "Database"
    }
}
