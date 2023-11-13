package com.niji.claudio.common.internal.repo

import com.niji.claudio.common.data.AbstractRepository
import com.niji.claudio.common.data.api.IClaudioApi
import com.niji.claudio.common.data.api.Resource
import com.niji.claudio.common.data.feature.media.IMediaRepository
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.save.IMediaDatabase
import com.niji.claudio.common.tool.LogUtils


class MediaRepository(
    private val api: IClaudioApi,
    private val database: IMediaDatabase,
) : AbstractRepository(), IMediaRepository {

    private var getMediasCache: MutableList<Media>? = null

    override suspend fun getLocalMedias(query: String, isFavoriteMode: Boolean): List<Media> {
        if (getMediasCache == null) {
            getMediasCache = database.getMedias().toMutableList()
        }
        return filterMedia(query, isFavoriteMode)
    }

    override suspend fun getMedias(query: String, isFavoriteMode: Boolean): List<Media> {
        if (mustReload) {
            val responseResource = sendGeneric(api::getMedias)
            if (responseResource is Resource.Success) {
                responseResource.data?.let {
                    database.saveMedias(it)
                    getMediasCache = it.toMutableList()
                    mustReload = false
                }
            }
        }
        if (getMediasCache == null) {
            getMediasCache = database.getMedias().toMutableList()
        }
        // Manage downloaded and favorite medias
        val downloadedMedias = database.getDownloadedMedias()
        val favoriteMedias = database.getFavoriteMedias()
        getMediasCache?.let { safeMediasCache ->
            downloadedMedias.forEach { downloadedMediaItem ->
                safeMediasCache.find { it.serverId == downloadedMediaItem.serverId }?.let { foundedMedia ->
                    safeMediasCache[safeMediasCache.indexOf(foundedMedia)] = foundedMedia.apply {
                        isDownloaded = true
                        isDownloadedState = true
                        filePath = downloadedMediaItem.filePath
                    }
                }
            }
            safeMediasCache.forEach { mediaItem ->
                safeMediasCache[safeMediasCache.indexOf(mediaItem)].apply {
                    val isFavoriteValue = favoriteMedias.find { it.serverId == mediaItem.serverId } != null
                    isFavorite = isFavoriteValue
                    isFavoriteState = isFavoriteValue
                }
            }
        }
        return filterMedia(query, isFavoriteMode)
    }

    private fun filterMedia(query: String, isFavoriteMode: Boolean): List<Media> {
        val copyMedias = getMediasCache?.let { ArrayList(it) }
        copyMedias?.sortByDescending { it.createdAt }
        return copyMedias?.filter {
            val filterCondition = (it.title?.contains(query, ignoreCase = true) == true
                    || it.category?.contains(query, ignoreCase = true) == true)
            if (isFavoriteMode) {
                (it.isFavorite ?: false) && filterCondition
            } else {
                filterCondition
            }
        } ?: emptyList()
    }

    override suspend fun getMediaFromServerId(serverId: String): Media? {
        val item = getMedias("", false).find { it.serverId == serverId }
        return if (item == null) {
            setMustReload()
            getMedias("", false).find { it.serverId == serverId }
        } else {
            item
        }
    }

    override suspend fun getMediaFromFilePath(filePath: String) =
        getMedias("", false).find { it.filePath == filePath }

    private suspend fun getMedia(id: String? = null): Media? {
        val responseResource = sendGeneric { api.getMedia(id) }
        return if (responseResource is Resource.Success) {
            LogUtils.d(TAG, "responseResource.data -> " + responseResource.data)
            responseResource.data
        } else {
            null
        }
    }

    override suspend fun downloadMedia(media: Media): Media? {
        return getMedia(media.serverId)?.url?.let {
            val downloadedMedia = api.downloadMedia(it, media)
            database.saveDownloadedMedia(
                Media(
                    serverId = downloadedMedia.serverId,
                    filePath = downloadedMedia.filePath
                )
            )
            downloadedMedia
        }
    }

    override suspend fun addMedia(media: Media): Media? {
        val responseResource = sendGeneric { api.addMedia(media) }
        return if (responseResource is Resource.Success) {
            setMustReload()
            media.apply {
                responseResource.data?.serverId
            }
        } else {
            null
        }
    }

    override suspend fun deleteMedia(media: Media) {
        val responseResource = sendGeneric { api.deleteMedia(media.serverId) }
        if (responseResource is Resource.Success) {
            database.deleteMedia(media)
            getMediasCache = responseResource.data?.toMutableList()
        }
    }

    override suspend fun deleteLocalMedia(media: Media) {
        database.deleteMedia(media)
        setMustReload()
        getMedias()
    }

    override suspend fun setFavoriteMediaUseCase(media: Media) {
        val favoriteMedias = database.getFavoriteMedias().toMutableList()
        if (favoriteMedias.find { it.serverId == media.serverId } == null) {
            favoriteMedias.add(Media(serverId = media.serverId))
            media.isFavorite = true
        } else {
            val favoriteMedia = favoriteMedias.find { it.serverId == media.serverId }
            favoriteMedia?.let { favoriteMedias.remove(it) }
            media.isFavorite = false
        }
        database.setFavoriteMedias(favoriteMedias)
    }

    companion object {
        private const val TAG = "MediaRepository"
    }
}