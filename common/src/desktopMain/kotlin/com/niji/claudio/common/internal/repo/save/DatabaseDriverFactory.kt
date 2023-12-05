package com.niji.claudio.common.internal.repo.save

import com.niji.claudio.common.tool.LogUtils
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:data/claudio.db")
        try {
            ClaudioDatabaseDelight.Schema.create(driver)
        } catch (e: Exception) {
            LogUtils.e(TAG, "Error in DatabaseDriverFactory", e)
        }
        return driver
    }

    companion object {
        private const val TAG = "DatabaseDriverFactory"
    }
}
