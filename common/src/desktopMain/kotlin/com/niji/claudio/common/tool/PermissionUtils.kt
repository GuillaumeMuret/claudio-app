package com.niji.claudio.common.tool

actual object PermissionUtils {
    actual fun isOverlayPermissionGranted() = true
    actual fun showOverlayPermission() {}
    actual fun isPermissionsGranted() = true
}