package com.niji.claudio.common.tool

import android.content.Context
import android.os.Build
import android.os.Environment
import com.niji.claudio.common.ClaudioApplication
import com.niji.claudio.common.R
import com.niji.claudio.common.data.model.MediaFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

actual object FileUtils {

    private const val DIR_NAME_ROOT = "claudio-media"
    private const val DIR_NAME_MEDIAS = "medias"
    private const val FILENAME_TLS = "tls.crt"

    actual fun fileExist(filePath: String) = File(filePath).exists()
    actual fun createMediaFile(filePath: String) = MediaFile(filePath)
    actual suspend fun generateTls(): String {
        val appContext = ClaudioApplication.applicationContext()
        val inputStream = appContext.resources.openRawResource(R.raw.tls)
        val destinationFile = File(appContext.filesDir, FILENAME_TLS)
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

    actual fun getMediasDirectoryPath(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/${DIR_NAME_ROOT}/${DIR_NAME_MEDIAS}"
        } else {
            ClaudioApplication.applicationContext().getDir(DIR_NAME_MEDIAS, Context.MODE_PRIVATE).path
        }
    }
}