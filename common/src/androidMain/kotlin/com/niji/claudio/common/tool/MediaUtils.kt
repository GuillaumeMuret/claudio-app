package com.niji.claudio.common.tool

import android.net.Uri
import com.niji.claudio.common.ClaudioApplication
import com.niji.claudio.common.data.model.Media
import java.io.File


actual object MediaUtils {

    actual fun getFileByteArray(media: Media): ByteArray? {
        return if (media.filePath?.startsWith("/") == false) {
            ClaudioApplication.applicationContext().contentResolver.openInputStream(
                Uri.parse(
                    media.filePath
                )
            )?.readBytes()
        } else {
            media.filePath?.let { File(it).readBytes() }
        }
    }

    actual fun fileChooser(
        window: Any,
        launchFileChooserIntent: (() -> Unit)?,
        allowedExtensions: List<String>,
        title: String,
        allowMultiSelection: Boolean
    ): Media? {
        launchFileChooserIntent?.invoke()
        return null
    }
}