package com.niji.claudio.common.tool

import android.content.Context


object UserPreferencesUtils {

    private const val APP_PREFERENCES_NAME = "UserPreferencesManager"
    const val DEVICE_PUSH_TOKEN_PREF = "device_push_token_pref"

    fun getBoolean(context: Context, name: String, defaultValue: Boolean = false) =
        getSharedPreferences(context).getBoolean(name, defaultValue)

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun put(context: Context, name: String, value: String) =
        getSharedPreferences(context).edit().putString(name, value).apply()

    fun getString(context: Context, name: String) =
        getSharedPreferences(context).getString(name, "")

    fun getStringOrNull(context: Context, name: String) =
        getSharedPreferences(context).getString(name, null)

    fun put(context: Context, name: String, value: Boolean) =
        getSharedPreferences(context).edit().putBoolean(name, value).apply()

    fun put(context: Context, name: String, value: Long) =
        getSharedPreferences(context).edit().putLong(name, value).apply()

    fun getInt(context: Context, name: String, defaultValue: Int) =
        getSharedPreferences(context).getInt(name, defaultValue)

    fun getLong(context: Context, name: String, defaultValue: Long) =
        getSharedPreferences(context).getLong(name, defaultValue)

    fun put(context: Context, name: String, value: Int) =
        getSharedPreferences(context).edit().putInt(name, value).apply()
}