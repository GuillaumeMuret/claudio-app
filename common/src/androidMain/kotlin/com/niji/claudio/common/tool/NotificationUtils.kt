package com.niji.claudio.common.tool

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.niji.claudio.common.ClaudioApplication
import com.niji.claudio.common.MainActivity
import com.niji.claudio.common.R

actual object NotificationUtils {
    private const val CHANNEL_ID = "Claudio"
    private var notificationId = 2

    actual fun createNotification(title: String, content: String) {
        val notificationIntent =
            Intent(ClaudioApplication.applicationContext(), MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val intent = PendingIntent.getActivity(
            ClaudioApplication.applicationContext(),
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification =
            NotificationCompat.Builder(ClaudioApplication.applicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_poop)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(NotificationCompat.BigTextStyle().bigText(content))
                .setContentIntent(intent)
                .build()
        with(NotificationManagerCompat.from(ClaudioApplication.applicationContext()))
        {
            if (ActivityCompat.checkSelfPermission(
                    ClaudioApplication.applicationContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel =
                    NotificationChannel(CHANNEL_ID, "Claudio", NotificationManager.IMPORTANCE_LOW)
                createNotificationChannel(notificationChannel)
            }
            notify(notificationId, notification)
            ++notificationId
        }
    }
}