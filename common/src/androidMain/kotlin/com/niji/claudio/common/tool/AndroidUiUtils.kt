package com.niji.claudio.common.tool

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.widget.Toast
import com.niji.claudio.common.ClaudioApplication


object AndroidUiUtils {
    fun makeToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                ClaudioApplication.applicationContext(),
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            // Method for API < 30
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }
}