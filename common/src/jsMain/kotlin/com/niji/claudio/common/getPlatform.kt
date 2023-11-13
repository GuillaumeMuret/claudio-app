package com.niji.claudio.common

import com.niji.claudio.common.ui.state.Platform


actual fun getPlatform(): Platform {
    return Platform.Web()
}

actual fun getPlatformName(): String {
    return "web"
}
