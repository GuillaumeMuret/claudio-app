package com.niji.claudio.common.ui.widget.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.ui.MediasViewModel
import com.niji.claudio.common.ui.widget.base.LineDivider
import com.niji.claudio.common.ui.widget.base.MediasToolbar
import com.niji.claudio.common.ui.widget.base.PulsatingCircle
import com.niji.claudio.common.ui.widget.base.SearchAppBar
import com.niji.claudio.common.ui.widget.base.TtsTextField
import com.niji.claudio.common.ui.widget.device.DropdownDevice


@Composable
fun MediasScreen(mVm: MediasViewModel, navController: NavHostController) {
    Column {
        MediasToolbar(mVm, navController)
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
        val appViewState = mVm.mediaScreenViewState.collectAsState()
        when (appViewState.value) {
            is MediaViewState.MediaDisplayColumn -> MediaListColumn(mVm)
            is MediaViewState.MediaDisplayGrid -> MediaListGrid(mVm)
            else -> MediaListGrid(mVm)
        }
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

@Composable
fun MediaListBase(mVm: MediasViewModel, isGrid: Boolean = false, content: @Composable (Media) -> Unit) {
    val mediaList = mVm.mediasState.collectAsState(listOf())
    val localFocusManager = LocalFocusManager.current
    val nestedScrollConnection: NestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                localFocusManager.clearFocus()
                return Offset.Zero
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            Box {
                LazyVerticalGrid(
                    modifier = Modifier.nestedScroll(nestedScrollConnection),
                    columns = if (isGrid) {
                        GridCells.Adaptive(150.dp)
                    } else {
                        GridCells.Adaptive(750.dp)
                    },
                    content = {
                        items(mediaList.value.size) { index ->
                            mediaList.value.getOrNull(index)?.let {
                                content(it)
                            }
                        }
                    }
                )
                val recordAudioState: Boolean by mVm.recordAudioState.collectAsState()
                if (recordAudioState) {
                    PulsatingCircle()
                }
            }
        }
    )
}

sealed class MediaViewState {
    data object MediaDisplayColumn : MediaViewState()
    data object MediaDisplayGrid : MediaViewState()
}

