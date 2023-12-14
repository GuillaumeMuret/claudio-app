package com.niji.claudio.common.tool

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.niji.claudio.common.ClaudioApplication


actual object PermissionUtils {
    private const val TAG = "PermissionUtils"
    actual fun isOverlayPermissionGranted() =
        Settings.canDrawOverlays(ClaudioApplication.applicationContext())

    actual fun showOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + ClaudioApplication.ANDROID_PACKAGE_NAME)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ClaudioApplication.applicationContext().startActivity(intent)
    }

    actual fun isPermissionsGranted(): Boolean {
        val permRecord = ContextCompat.checkSelfPermission(
            ClaudioApplication.applicationContext(),
            Manifest.permission.RECORD_AUDIO
        )
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val permStorage = Environment.isExternalStorageManager()
            permStorage && permRecord == PackageManager.PERMISSION_GRANTED
        } else {
            val permRead = ContextCompat.checkSelfPermission(
                ClaudioApplication.applicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val permWrite = ContextCompat.checkSelfPermission(
                ClaudioApplication.applicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            permRead == PackageManager.PERMISSION_GRANTED &&
                    permWrite == PackageManager.PERMISSION_GRANTED &&
                    permRecord == PackageManager.PERMISSION_GRANTED &&
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val permNotification = ContextCompat.checkSelfPermission(
                            ClaudioApplication.applicationContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                        permNotification == PackageManager.PERMISSION_GRANTED
                    } else {
                        true
                    }
        }
    }
}