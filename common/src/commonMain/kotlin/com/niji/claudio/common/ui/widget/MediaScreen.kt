package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.niji.claudio.common.ui.MediasViewModel
import com.niji.claudio.common.ui.state.AppViewState


@Composable
fun MediasScreen(mVm: MediasViewModel) {
    MediasToolbar(mVm)
    DropdownDeviceWithSearch(mVm)
    MediaRemote(
        killPlayer = mVm::killPlayer,
        volumeMin = mVm::volumeMin,
        volumeLower = mVm::volumeLower,
        volumeRaise = mVm::volumeRaise,
        volumeMax = mVm::volumeMax,
        toggleSleepingMode = mVm::toggleSleepingMode,
        startRecording = mVm::startRecording,
        stopRecording = mVm::stopRecording,
        isSleepingMode = mVm.isSleepingMode,
        mustReload = mVm::refreshMedia
    )
    TtsTextField(mVm)
    when (mVm.appViewState.value) {
        is AppViewState.MediaDisplayColumn -> MediaListColumn(mVm)
        is AppViewState.MediaDisplayGrid -> MediaListGrid(mVm)
        else -> MediaListGrid(mVm)
    }
}

@Composable
fun DropdownDeviceWithSearch(mVm: MediasViewModel) {
    Row {
        DropdownDevice(mVm)
        SearchAppBar(mVm)
    }
    LineDivider()
}
