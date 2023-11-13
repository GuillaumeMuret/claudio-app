package com.niji.claudio.common.tool

import androidx.compose.ui.awt.ComposeWindow
import com.niji.claudio.common.data.model.Media
import java.awt.FileDialog
import java.io.File

actual object MediaUtils {

    actual fun getFileByteArray(media: Media): ByteArray? {
        return media.filePath?.let { File(it).readBytes() }
    }

    actual fun fileChooser(
        window: Any,
        launchFileChooserIntent: (() -> Unit)?,
        allowedExtensions: List<String>,
        title: String,
        allowMultiSelection: Boolean
    ): Media? {
        return if (window is ComposeWindow) {
            FileDialog(window, title, FileDialog.LOAD).apply {
                isMultipleMode = allowMultiSelection
                // windows
                file = allowedExtensions.joinToString(";") { "*$it" }
                // linux
                setFilenameFilter { _, name -> allowedExtensions.any { name.endsWith(it) } }
                isVisible = true
            }.files.getOrNull(0)?.let {
                Media(filename = it.name, filePath = it.path)
            }
        } else {
            null
        }
    }
}