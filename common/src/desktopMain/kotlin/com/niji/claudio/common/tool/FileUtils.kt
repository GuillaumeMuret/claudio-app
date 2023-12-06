package com.niji.claudio.common.tool

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.ResourceLoader
import com.niji.claudio.common.data.model.MediaFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

actual object FileUtils {

    private const val DIR_NAME_ROOT = "data"
    private const val DIR_NAME_MEDIAS = "medias"
    private const val DIR_NAME_RAW = "raw"
    private const val FILENAME_TLS = "tls.crt"

    actual fun fileExist(filePath: String) = File(filePath).exists()
    actual fun createMediaFile(filePath: String) = MediaFile(filePath)

    @OptIn(ExperimentalComposeUiApi::class)
    actual suspend fun generateTls(): String {
        val inputStream = ResourceLoader.Default.load("${DIR_NAME_RAW}/${FILENAME_TLS}")
        val destinationFile = File("./${DIR_NAME_ROOT}", FILENAME_TLS)
        if (!destinationFile.exists()) {
            withContext(Dispatchers.IO) {
                val outputStream = FileOutputStream(destinationFile)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
            }
        }
        return destinationFile.path
    }

    actual fun getMediasDirectoryPath(): String = File("./${DIR_NAME_ROOT}/${DIR_NAME_MEDIAS}").path
}
