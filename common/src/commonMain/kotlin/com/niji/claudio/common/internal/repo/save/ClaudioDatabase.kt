package com.niji.claudio.common.internal.repo.save

import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.data.save.IClaudioDatabase

expect class ClaudioDatabase() : IClaudioDatabase {
    override suspend fun getDevices(): List<Device>
    override suspend fun getMedias(): List<Media>
    override suspend fun getDownloadedMedias(): MutableList<Media>
    override suspend fun getFavoriteMedias(): MutableList<Media>
    override suspend fun setFavoriteMedias(medias: List<Media>)
    override suspend fun saveDevices(devices: List<Device>)
    override suspend fun deleteDevice(device: Device)
    override suspend fun deleteAllDevice()
    override suspend fun saveMedias(medias: List<Media>)
    override suspend fun saveDownloadedMedia(downloadedMedia: Media)
    override suspend fun deleteMedia(media: Media)
    override suspend fun getUser(): User
    override suspend fun addUser(user: User): User
    override suspend fun updateUser(user: User)
    override suspend fun getDataLogs(): List<DataLog>
    override suspend fun addDataLog(dataLog: DataLog)
    suspend fun saveDownloadedMedias(downloadedMedias: List<Media>)
    fun getMediasDirectoryPath(): String
}
