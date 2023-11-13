package com.niji.claudio.common.data.feature.media

import com.niji.claudio.common.data.IAbstractRepository
import com.niji.claudio.common.data.model.Media

interface IMediaRepository : IAbstractRepository {
    suspend fun getLocalMedias(query: String = "", isFavoriteMode: Boolean = false): List<Media>
    suspend fun getMedias(query: String = "", isFavoriteMode: Boolean = false): List<Media>
    suspend fun getMediaFromServerId(serverId: String): Media?
    suspend fun getMediaFromFilePath(filePath: String): Media?
    suspend fun deleteMedia(media: Media)
    suspend fun deleteLocalMedia(media: Media)
    suspend fun setFavoriteMediaUseCase(media: Media)
    suspend fun downloadMedia(media: Media): Media?
    suspend fun addMedia(media: Media): Media?
}