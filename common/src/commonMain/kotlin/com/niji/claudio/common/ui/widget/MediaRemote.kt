package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.niji.claudio.common.data.feature.user.usecase.ToggleAdminProcessUseCase
import com.niji.claudio.common.ui.theme.indigo
import com.niji.claudio.common.ui.theme.mute
import com.niji.claudio.common.ui.theme.orange
import com.niji.claudio.common.ui.theme.red
import com.niji.claudio.common.ui.theme.yellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@Composable
fun MediaRemote(
    killPlayer: () -> Unit,
    volumeMin: () -> Unit,
    volumeLower: () -> Unit,
    volumeRaise: () -> Unit,
    volumeMax: () -> Unit,
    toggleSleepingMode: () -> Unit,
    startRecording: () -> Unit,
    stopRecording: () -> Unit,
    isSleepingMode: MutableStateFlow<Boolean>,
    mustReload: () -> Unit
) {
    Box {
        Column {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                IconMediaRemote(
                    painter = ClaudioPainterResource.get(ClaudioPainterResourceId.IC_BOLT),
                    color = MaterialTheme.colors.yellow,
                    contentDescription = "Kill them all",
                    action = { killPlayer.invoke() },
                    mustReload = mustReload
                )
                IconMediaRemote(
                    painter = ClaudioPainterResource.get(ClaudioPainterResourceId.IC_MUTE),
                    color = MaterialTheme.colors.mute,
                    contentDescription = "Volume mute",
                    action = { volumeMin.invoke() },
                    mustReload = mustReload
                )
                IconMediaRemote(
                    painter = ClaudioPainterResource.get(ClaudioPainterResourceId.IC_VOLUME_DOWN),
                    color = MaterialTheme.colors.indigo,
                    contentDescription = "Volume down",
                    action = { volumeLower.invoke() },
                    mustReload = mustReload
                )
                IconMediaRemote(
                    painter = ClaudioPainterResource.get(ClaudioPainterResourceId.IC_VOLUME_UP),
                    color = MaterialTheme.colors.red,
                    contentDescription = "Volume up",
                    action = { volumeRaise.invoke() },
                    mustReload = mustReload
                )
                IconMediaRemote(
                    painter = ClaudioPainterResource.get(ClaudioPainterResourceId.IC_FIRE),
                    color = MaterialTheme.colors.orange,
                    contentDescription = "Volume max",
                    action = { volumeMax.invoke() },
                    mustReload = mustReload
                )
                Image(
                    painter = ClaudioPainterResource.get(ClaudioPainterResourceId.IC_MICRO),
                    contentDescription = "Micro",
                    modifier = Modifier.pointerInput(true) {
                        detectTapGestures(onPress = {
                            coroutineScope {
                                startRecording()
                                tryAwaitRelease()
                                stopRecording()
                            }
                        })
                    }
                        .size(50.dp)
                        .padding(10.dp)
                )
                val isSleepingModeState: Boolean by isSleepingMode.collectAsState()
                IconMediaRemote(
                    image = if (isSleepingModeState) {
                        ClaudioPainterResource.get(ClaudioPainterResourceId.IC_SLEEP)
                    } else {
                        ClaudioPainterResource.get(ClaudioPainterResourceId.IC_SMILE)
                    },
                    contentDescription = "Toggle sleep or awake mode",
                    action = { toggleSleepingMode.invoke() },
                    mustReload = mustReload
                )
            }
            RemoteDivider()
        }
    }
}

@Composable
fun IconMediaRemote(
    imageVector: ImageVector? = null,
    color: Color = MaterialTheme.colors.primary,
    painter: Painter? = null,
    image: Painter? = null,
    contentDescription: String,
    action: () -> Unit,
    mustReload: () -> Unit
) {
    IconButton(onClick = {
        action.invoke()
        CoroutineScope(Dispatchers.Default).launch {
            val mustReloadFlag = ToggleAdminProcessUseCase(contentDescription).execute()
            if (mustReloadFlag) {
                mustReload()
            }
        }
    }) {
        imageVector?.let {
            Icon(
                imageVector = it,
                contentDescription = contentDescription,
                tint = color,
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp)
            )
        }
        painter?.let {
            Icon(
                painter = it,
                contentDescription = contentDescription,
                tint = color,
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp)
            )
        }
        image?.let {
            Image(
                painter = it,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp)
            )
        }
    }
}
