package com.niji.claudio.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.niji.claudio.common.data.feature.user.usecase.GetUserUseCase
import com.niji.claudio.common.tool.LogUtils
import com.niji.claudio.common.tool.MqttClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MqttService : Service() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        startForeground(NOTIFICATION_ID, createNotification())
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val user = GetUserUseCase().execute()
                user.mDeviceServerId?.let {
                    MqttClientProvider.initAndRunClient(it)
                }
                delay(1_000)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtils.d(TAG, "onStartCommand $intent - $flags - $startId")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        LogUtils.d(TAG, "onBind $intent")
        return null
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Mqtt Service Channel", NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        val snoozeIntent = Intent(this, MqttBroadcastReceiver::class.java).apply {
            action = EXIT_APPLICATION
        }
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder =
            NotificationCompat.Builder(ClaudioApplication.applicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_poop)
                .setContentTitle("Claudio \uD83D\uDCA9")
                .setContentText("Service running \uD83C\uDF89")
                .addAction(
                    R.drawable.ic_launcher_foreground, "Quit application", snoozePendingIntent
                ).setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return builder.build()
    }

    internal class MqttBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == EXIT_APPLICATION) {
                MainActivity.instance?.finishAndRemoveTask()
                instance.stopForeground(STOP_FOREGROUND_REMOVE)
                instance.stopSelf()
            }
        }
    }

    companion object {
        private const val TAG = "MqttService"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "MqttServiceChannel"
        const val EXIT_APPLICATION = "com.claudio.android.EXIT_APPLICATION"

        lateinit var instance: MqttService
    }
}