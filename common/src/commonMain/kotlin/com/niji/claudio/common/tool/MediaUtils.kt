package com.niji.claudio.common.tool

import com.niji.claudio.common.data.model.Media


expect object MediaUtils {
    fun getFileByteArray(media: Media): ByteArray?
    fun fileChooser(
        window: Any,
        launchFileChooserIntent: (() -> Unit)? = null,
        allowedExtensions: List<String> = listOf(".mp4", ".mp3", ".ogg", ".m4a"),
        title: String = "Choose a file",
        allowMultiSelection: Boolean = false
    ): Media?
}