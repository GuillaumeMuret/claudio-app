package com.niji.claudio.common.internal.repo.save

import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.data.save.IClaudioDatabase

internal class Database(databaseDriverFactory: DatabaseDriverFactory): IClaudioDatabase {
    private val database = ClaudioDatabaseDelight(databaseDriverFactory.createDriver())
    private val dbQuery = database.claudioDatabaseDelightQueries

    override suspend fun getDevices(): List<Device> {
        TODO("Not yet implemented")
    }

    override suspend fun saveDevices(devices: List<Device>) {
        TODO("Not yet implemented")
    }

    override suspend fun setFavoriteMedias(medias: List<Media>) {
        saveMedias(medias)
    }

    override suspend fun saveDownloadedMedia(downloadedMedia: Media) {
        saveMedias(listOf(downloadedMedia))
    }

    override suspend fun deleteMedia(media: Media) {
        media.serverId?.let { deleteMediaByServerId(it) }
    }

    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(user: User): User {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getDataLogs(): List<DataLog> {
        TODO("Not yet implemented")
    }

    override suspend fun addDataLog(dataLog: DataLog) {
        TODO("Not yet implemented")
    }

    override suspend fun saveMedias(medias: List<Media>) {
        dbQuery.transaction {
            medias.forEach { media ->
                media.serverId?.let { serverId ->
                    dbQuery.saveMedia(
                        serverId = serverId,
                        filePath = media.filePath,
                        filename = media.filename,
                        durationSec = media.durationSec?.toString(),
                        url = media.url,
                        title = media.title,
                        category = media.category,
                        isDownloaded = media.isDownloaded,
                        playCount = media.playCount?.toLong(),
                        size = media.size?.toLong(),
                        fromTitle = media.fromTitle,
                        isFavorite = media.isFavorite,
                        createdAt = media.createdAt,
                    )
                }
            }
        }
    }

    private fun deleteMediaByServerId(serverId: String) {
        dbQuery.transaction {
            dbQuery.deleteMediaByServerId(serverId)
        }
    }

    override suspend fun getMedias(): List<Media> {
        return dbQuery.getMedias(::mapMediaSelecting).executeAsList()
    }

    override suspend fun getDownloadedMedias(): List<Media> {
        return dbQuery.getDownloadedMedias(::mapMediaSelecting).executeAsList()
    }

    override suspend fun getFavoriteMedias(): List<Media> {
        return dbQuery.getFavoriteMedias(::mapMediaSelecting).executeAsList()
    }

    private fun mapMediaSelecting(
        serverId: String?,
        filePath: String?,
        filename: String?,
        durationSec: String?,
        url: String?,
        title: String?,
        category: String?,
        isDownloaded: Boolean?,
        playCount: Long?,
        size: Long?,
        fromTitle: String?,
        isFavorite: Boolean?,
        createdAt: String?,
    ): Media {
        return Media(
            serverId = serverId,
            filePath = filePath,
            filename = filename,
            durationSec = durationSec?.toFloat(),
            url = url,
            title = title,
            category = category,
            isDownloaded = isDownloaded == true,
            playCount = playCount?.toInt(),
            size = size?.toInt(),
            fromTitle = fromTitle,
            isFavorite = isFavorite == true,
            createdAt = createdAt,
        )
    }
}
