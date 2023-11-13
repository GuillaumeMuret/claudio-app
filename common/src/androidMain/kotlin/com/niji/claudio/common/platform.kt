package com.niji.claudio.common

import com.niji.claudio.common.ui.state.Platform
import com.niji.claudio.common.ui.state.Type


actual fun getPlatform(): Platform {
    return Platform.Mobile(Type.Android)
}

actual fun getPlatformName(): String {
    return "Android"
}