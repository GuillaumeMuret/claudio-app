package com.niji.claudio.common.internal.repo.save

import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.data.save.IClaudioDatabase
import com.niji.claudio.common.tool.LogUtils

internal class Database(databaseDriverFactory: DatabaseDriverFactory) : IClaudioDatabase {
    private val database = ClaudioDatabaseDelight(databaseDriverFactory.createDriver())
    private val dbQuery = database.claudioDatabaseDelightQueries

    // ############################################################################
    // Medias
    // ############################################################################

    override suspend fun getMedias() =
        dbQuery.transactionWithResult { dbQuery.getMedias(::mapMediaSelecting).executeAsList() }

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

    override suspend fun setFavoriteMedias(medias: List<Media>) {
        saveMedias(medias)
    }

    override suspend fun saveDownloadedMedia(downloadedMedia: Media) {
        saveMedias(listOf(downloadedMedia))
    }

    override suspend fun deleteMedia(media: Media) {
        media.serverId?.let { deleteMediaByServerId(it) }
    }

    private fun deleteMediaByServerId(serverId: String) {
        dbQuery.transaction {
            dbQuery.deleteMediaByServerId(serverId)
        }
    }

    override suspend fun getDownloadedMedias() = dbQuery.transactionWithResult {
        dbQuery.getDownloadedMedias(::mapMediaSelecting).executeAsList()
    }

    override suspend fun getFavoriteMedias() = dbQuery.transactionWithResult {
        dbQuery.getFavoriteMedias(::mapMediaSelecting).executeAsList()
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

// ############################################################################
// Devices
// ############################################################################

    override suspend fun getDevices() = dbQuery.transactionWithResult {
        dbQuery.getDevices(::mapDeviceSelecting).executeAsList()
    }

    override suspend fun saveDevices(devices: List<Device>) {
        dbQuery.transaction {
            devices.forEach { device ->
                device.serverId?.let { serverId ->
                    dbQuery.addDevice(
                        serverId = serverId,
                        name = device.name,
                        pushToken = device.pushToken,
                    )
                }
            }
        }
    }

    override suspend fun deleteDevice(device: Device) {
        dbQuery.transaction {
            device.serverId?.let { dbQuery.deleteDeviceByServerId(it) }
        }
    }

    override suspend fun deleteAllDevice() {
        dbQuery.transaction {
            dbQuery.deleteAllDevice()
        }
    }

    private fun mapDeviceSelecting(
        serverId: String?,
        name: String?,
        pushToken: String?,
    ): Device {
        return Device(
            serverId = serverId,
            name = name,
            pushToken = pushToken
        )
    }

// ############################################################################
// User
// ############################################################################

    override suspend fun getUser(): User {
        return try {
            dbQuery.transactionWithResult {
                dbQuery.getUser(USER_BDD_ID_DEFAULT, ::mapUserSelecting).executeAsOne()
            }
        } catch (npe: NullPointerException) {
            LogUtils.d(TAG, "No user found")
            addUser(User())
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
        dbQuery.transaction {
            dbQuery.upsertUser(
                bddId = USER_BDD_ID_DEFAULT,
                name = user.name,
                mPushToken = user.mPushToken,
                mDeviceServerId = user.mDeviceServerId,
                selectedServerIdDevice = user.selectedServerIdDevice,
                mediaDisplayPreference = user.mediaDisplayPreference,
                isSleepingMode = user.isSleepingMode,
                isAdmin = user.isAdmin,
            )
        }
    }

    private fun mapUserSelecting(
        bddId: Long,
        name: String?,
        mPushToken: String?,
        mDeviceServerId: String?,
        selectedServerIdDevice: String?,
        mediaDisplayPreference: String?,
        isSleepingMode: Boolean?,
        isAdmin: Boolean?,
    ): User {
        return User(
            bddId = bddId,
            name = name,
            mPushToken = mPushToken,
            mDeviceServerId = mDeviceServerId,
            selectedServerIdDevice = selectedServerIdDevice,
            mediaDisplayPreference = mediaDisplayPreference,
            isSleepingMode = isSleepingMode == true,
            isAdmin = isAdmin == true,
        )
    }

// ############################################################################
// DataLog
// ############################################################################

    override suspend fun getDataLogs(): List<DataLog> = dbQuery.transactionWithResult {
        dbQuery.getDataLogs(::mapDataLogsSelecting).executeAsList()
    }

    override suspend fun addDataLog(dataLog: DataLog) {
        dbQuery.transaction {
            dbQuery.addDataLog(
                dateStr = dataLog.dateStr,
                action = dataLog.action,
                isIgnored = dataLog.isIgnored,
                data_ = dataLog.data,
            )
        }
    }

    private fun mapDataLogsSelecting(
        dateStr: String,
        action: String?,
        isIgnored: Boolean?,
        data: String?,
    ): DataLog {
        return DataLog(
            dateStr = dateStr,
            action = action,
            isIgnored = isIgnored == true,
            data = data,
        )
    }

    companion object {
        private const val TAG = "Database"
        private const val USER_BDD_ID_DEFAULT = 0L
    }
}
