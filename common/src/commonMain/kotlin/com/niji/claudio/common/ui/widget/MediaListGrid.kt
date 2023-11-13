package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niji.claudio.common.tool.UiUtils
import com.niji.claudio.common.ui.MediasViewModel
import com.niji.claudio.common.ui.theme.mediaGridCards
import com.niji.claudio.common.ui.theme.red
import kotlin.math.roundToInt


@Composable
fun MediaListGrid(mVm: MediasViewModel) {
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
                    columns = GridCells.Adaptive(150.dp),
                    content = {
                        items(mediaList.value.size) { index ->
                            mediaList.value.getOrNull(index)?.let {
                                val downloadProgressState = it.downloadProgressState
                                Card(
                                    modifier = Modifier
                                        .defaultMinSize(150.dp, 150.dp)
                                        .padding(5.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable(onClick = { mVm.mediaPlay(it) })
                                ) {
                                    Box(
                                        modifier = Modifier.background(color = MaterialTheme.colors.mediaGridCards),
                                        content = {
                                            val color = Color.White
                                            Column(
                                                horizontalAlignment = Alignment.Start,
                                                modifier = Modifier.padding(5.dp),
                                                content = {
                                                    Text(
                                                        text = it.title ?: "",
                                                        fontSize = 16.sp,
                                                        maxLines = 2,
                                                        overflow = TextOverflow.Ellipsis,
                                                        style = MaterialTheme.typography.body1.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            color = color
                                                        )
                                                    )
                                                    it.category?.let {
                                                        Text(
                                                            text = it,
                                                            color = color,
                                                            fontSize = 14.6.sp
                                                        )
                                                    }
                                                    it.size?.let {
                                                        Text(
                                                            text = UiUtils.getFriendlySize(it),
                                                            color = color,
                                                            fontSize = 14.6.sp
                                                        )
                                                    }
                                                    it.durationSec?.let { duration ->
                                                        Text(
                                                            text = "${(duration * 10.0).roundToInt() / 10.0}s",
                                                            color = color,
                                                            fontSize = 14.6.sp
                                                        )
                                                    }
                                                })
                                            Box(
                                                modifier = Modifier.padding(5.dp)
                                                    .align(Alignment.BottomEnd),
                                                content = {
                                                    if ((downloadProgressState?.progress
                                                            ?: 0) < (downloadProgressState?.max
                                                            ?: 0)
                                                    ) {
                                                        downloadProgressState?.let { state ->
                                                            LinearProgressIndicator(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(top = 10.dp),
                                                                color = color,
                                                                progress = (state.progress.toFloat() / state.max.toFloat())
                                                            )
                                                        }
                                                    } else {
                                                        IconButton(
                                                            onClick = {
                                                                it.isFavoriteState =
                                                                    it.isFavorite == null || it.isFavorite == false
                                                                mVm.setFavoriteMedia(it)
                                                            },
                                                            modifier = Modifier.align(Alignment.BottomEnd)
                                                        ) {
                                                            Icon(
                                                                Icons.Filled.Favorite,
                                                                "Favorite",
                                                                tint = if (it.isFavoriteState == true) {
                                                                    MaterialTheme.colors.red
                                                                } else {
                                                                    Color.Gray
                                                                },
                                                                modifier = Modifier.size(30.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    )
                                }
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
