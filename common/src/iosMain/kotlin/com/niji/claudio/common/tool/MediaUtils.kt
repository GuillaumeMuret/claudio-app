package com.niji.claudio.common.tool

import com.niji.claudio.common.data.model.Media

actual object MediaUtils {
    actual fun getFileByteArray(media: Media): ByteArray? {
        TODO("Not yet implemented")
    }

    actual fun fileChooser(
        window: Any,
        launchFileChooserIntent: (() -> Unit)?,
        allowedExtensions: List<String>,
        title: String,
        allowMultiSelection: Boolean
    ): Media? {
        TODO("Not yet implemented")
    }
}