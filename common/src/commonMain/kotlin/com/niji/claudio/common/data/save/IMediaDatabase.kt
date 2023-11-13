package com.niji.claudio.common.data.save

import com.niji.claudio.common.data.model.Media

interface IMediaDatabase {
    suspend fun getMedias(): List<Media>
    suspend fun getDownloadedMedias(): List<Media>
    suspend fun getFavoriteMedias(): List<Media>
    suspend fun setFavoriteMedias(medias: List<Media>)
    suspend fun saveMedias(medias: List<Media>)
    suspend fun saveDownloadedMedia(downloadedMedia: Media)
    suspend fun deleteMedia(media: Media)
}