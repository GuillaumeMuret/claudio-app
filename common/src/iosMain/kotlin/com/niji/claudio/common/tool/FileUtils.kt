package com.niji.claudio.common.tool

import com.niji.claudio.common.data.model.MediaFile
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding

actual object FileUtils {
    private const val TAG = "FileUtils"

    actual fun fileExist(filePath: String) = false
    actual fun createMediaFile(filePath: String) = MediaFile(filePath)

    actual fun getMediasDirectoryPath(): String {
        // TODO("Not yet implemented")
        return ""
    }

    actual suspend fun generateTls(): String {
        // TODO("Not yet implemented")
        return ""
    }

    @OptIn(ExperimentalForeignApi::class)
    fun writeFile(filename: String, content: String) {
        val fileManager = NSFileManager.defaultManager
        val fileUrl = (fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .last() as? NSURL)?.URLByAppendingPathComponent(filename)
        LogUtils.d(TAG, "write file -> $fileUrl")
        val nsContent: NSData? = memScoped {
            val string = NSString.create(string = content)
            string.dataUsingEncoding(NSUTF8StringEncoding)
        }
        memScoped {
            fileUrl?.path?.let { safePath ->
                content.usePinned {
                    fileManager.createFileAtPath(safePath, nsContent, null)
                }
            }
        }
    }

    fun readFile(filename: String): String {
        val fileManager = NSFileManager.defaultManager
        val fileUrl = (fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .last() as? NSURL)?.URLByAppendingPathComponent(filename)
        val nsContent = fileManager.contentsAtPath(fileUrl?.path ?: "")
        val content = nsContent?.let { NSString.create(it, NSUTF8StringEncoding) } as? String
        return content ?: ""
    }
}