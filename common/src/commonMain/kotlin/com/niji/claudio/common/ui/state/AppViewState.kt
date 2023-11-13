package com.niji.claudio.common.ui.state

sealed class AppViewState {
    data object MediaDisplayColumn : AppViewState()
    data object MediaDisplayGrid : AppViewState()
    data object DeviceColumn : AppViewState()
    data object DataLog : AppViewState()
}
