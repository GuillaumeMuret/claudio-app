package com.niji.claudio.common.data.feature.player.usecase

import com.niji.claudio.common.data.feature.media.usecase.DownloadMediaUseCase
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.tool.FileUtils
import com.niji.claudio.common.tool.LogUtils
import com.niji.claudio.common.tool.PlayerUtils


class PlayMediaLocalUseCase(val media: Media) {
    suspend fun execute() {
        if (media.isDownloaded == true && media.filePath?.let { FileUtils.fileExist(it) } == true) {
            PlayerUtils.play(media)
        } else {
            LogUtils.d(TAG, "mediaPlay no file")
            val downloadedMedia = DownloadMediaUseCase(media).execute()
            if (downloadedMedia != null) {
                PlayerUtils.play(downloadedMedia)
            }
        }
    }

    companion object {
        private const val TAG = "PlayMediaLocal"
    }
}
