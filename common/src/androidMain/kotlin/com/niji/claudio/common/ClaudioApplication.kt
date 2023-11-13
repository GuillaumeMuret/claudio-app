package com.niji.claudio.common

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import kotlin.system.exitProcess


class ClaudioApplication : Application() {

    init {
        instance = this
    }

    fun restartApp() {
        val mStartActivity = packageManager.getLaunchIntentForPackage(ANDROID_PACKAGE_NAME)
        // val mStartActivity = Intent(applicationContext(), MainActivity::class.java)
        val mPendingIntentId = PENDING_INTENT_ID
        val mPendingIntent = PendingIntent.getActivity(
            applicationContext(),
            mPendingIntentId,
            mStartActivity,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        val manager = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        manager.set(
            AlarmManager.RTC,
            System.currentTimeMillis() + TIME_AFTER_RESTART_MS,
            mPendingIntent
        )
        exitProcess(0)
    }

    companion object {
        const val ANDROID_PACKAGE_NAME = "com.niji.claudio.android"
        private const val TAG = "ClaudioApplication"
        private const val TIME_AFTER_RESTART_MS = 100L
        private const val PENDING_INTENT_ID = 123456
        private lateinit var instance: ClaudioApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }

        fun restartApp() {
            return instance.restartApp()
        }
    }
}