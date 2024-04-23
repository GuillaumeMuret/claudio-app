package com.niji.claudio.common.data.model


expect class Media(
    bddId: Long? = null,
    // File path where video has been downloaded
    filePath: String? = null,
    // File name
    filename: String? = null,
    // Unique sync with backend and front
    serverId: String? = null,
    // Unique sync with backend and front
    durationSec: Float? = null,
    // Url of the media
    url: String? = null,
    // Title. If not provided, title is the internet video title
    title: String? = null,
    // Category of the media
    category: String? = null,
    // Correctly downloaded
    isDownloaded: Boolean? = null,
    // play count number
    playCount: Int? = null,
    // file size in byte
    size: Int? = null,
    // The signature of the sender. Usually the username when sending something
    fromTitle: String? = null,
    // is favorite media
    isFavorite: Boolean? = null,
    // file creation date
    createdAt: String? = null,
) {
    var bddId: Long?
    var filePath: String?
    var filename: String?
    var serverId: String?
    var durationSec: Float?
    val url: String?
    var title: String?
    var category: String?
    var isDownloaded: Boolean?
    var playCount: Int?
    var size: Int?
    var fromTitle: String?
    var isFavorite: Boolean?
    var createdAt: String?

    var isDownloadedState: Boolean?
    var isFavoriteState: Boolean?
    var downloadProgressState: DownloadProgress?
}
