package com.niji.claudio.common.tool

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual object DateUtils {

    actual fun getCurrentDateStrType1(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }
}