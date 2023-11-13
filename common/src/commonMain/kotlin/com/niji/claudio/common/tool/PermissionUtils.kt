package com.niji.claudio.common.tool

expect object PermissionUtils {
    fun isPermissionsGranted(): Boolean
    fun isOverlayPermissionGranted(): Boolean
    fun showOverlayPermission()
}