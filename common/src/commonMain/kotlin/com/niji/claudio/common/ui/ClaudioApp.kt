package com.niji.claudio.common.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.niji.claudio.common.ui.state.AppViewState
import com.niji.claudio.common.ui.widget.DataLogList
import com.niji.claudio.common.ui.widget.DataLogToolbar
import com.niji.claudio.common.ui.widget.DeviceList
import com.niji.claudio.common.ui.widget.DevicesToolbar
import com.niji.claudio.common.ui.widget.DialogAddMedia
import com.niji.claudio.common.ui.widget.DialogError
import com.niji.claudio.common.ui.widget.DialogOverApp
import com.niji.claudio.common.ui.widget.DialogUserName
import com.niji.claudio.common.ui.widget.MediasScreen


@Composable
fun ClaudioApp(
    mVm: MediasViewModel,
    window: Any? = null,
    launchFileChooserIntent: (() -> Unit)? = null
) {
    val showDeviceDialogState: Boolean by mVm.showDeviceDialogState.collectAsState()
    val showOverAppDialogState: Boolean by mVm.showOverAppDialogState.collectAsState()
    val showErrorDialogState: Boolean by mVm.showErrorDialogState.collectAsState()
    val appViewState = mVm.appViewState.collectAsState()
    val localFocusManager = LocalFocusManager.current
    mVm.initMediaScreen()
    Surface(
        color = MaterialTheme.colors.background
    ) {
        DialogError(show = showErrorDialogState, onDismiss = mVm::onErrorDialogDismiss)
        DialogOverApp(
            show = showOverAppDialogState,
            onDismiss = mVm::onOverAppDialogDismiss,
            onConfirm = mVm::onOverAppDialogConfirm
        )
        DialogUserName(
            show = showDeviceDialogState,
            onDismiss = mVm::onDeviceDialogDismiss,
            onConfirm = mVm::onDeviceDialogConfirm,
            mVm
        )
        DialogAddMedia(
            mVm,
            window,
            launchFileChooserIntent
        )
        Column(modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
            })
        }) {
            when (appViewState.value) {
                is AppViewState.MediaDisplayColumn,
                is AppViewState.MediaDisplayGrid -> MediasScreen(mVm)

                is AppViewState.DeviceColumn -> {
                    DevicesToolbar(mVm)
                    DeviceList(mVm)
                }

                is AppViewState.DataLog -> {
                    DataLogToolbar(mVm)
                    DataLogList(mVm)
                }

                else -> {}
            }
        }
    }
}
