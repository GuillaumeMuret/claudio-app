package com.niji.claudio.common.internal.repo.save

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {
    fun createDriver(): SqlDriver
}
