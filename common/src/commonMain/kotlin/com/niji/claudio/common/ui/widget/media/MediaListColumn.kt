package com.niji.claudio.common.ui.widget.media

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.resources.Res
import com.niji.claudio.common.resources.ic_download
import com.niji.claudio.common.tool.UiUtils
import com.niji.claudio.common.ui.MediasViewModel
import com.niji.claudio.common.ui.theme.red
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt


@Composable
fun MediaListColumn(mVm: MediasViewModel) {
    MediaListBase(mVm, false) {
        MediaListColumnItem(mVm, it)
    }
}

@Composable
fun MediaListColumnItem(mVm: MediasViewModel, media: Media) {
    val itemSize = 80
    val isAdminState = mVm.isAdminState.collectAsState()
    val downloadProgressState = media.downloadProgressState
    val isDownloadedState = media.isDownloadedState
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { mVm.mediaPlay(media) })
            .padding(horizontal = 16.dp, vertical = 8.dp), content = {
            val color = MaterialTheme.colors.primary
            Box(
                content = {
                    Text(
                        text = UiUtils.getFriendlySize(media.size ?: 0),
                        fontSize = 24.sp,
                        color = MaterialTheme.colors.onBackground
                    )
                }, modifier = Modifier
                    .size(itemSize.dp)
                    .border(
                        width = 1.2.dp,
                        color = MaterialTheme.colors.onBackground,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier.weight(2F),
                content = {
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = media.title ?: "",
                        color = color,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    media.category?.let {
                        Text(
                            text = it,
                            color = color,
                            fontSize = 14.6.sp
                        )
                    }
                    media.durationSec?.let { duration ->
                        Text(
                            text = "${(duration * 10.0).roundToInt() / 10.0}s",
                            color = Color.Gray,
                            fontSize = 14.6.sp
                        )
                    }
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
                        Text(
                            text = media.filename.toString(),
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                })
            Spacer(modifier = Modifier.size(16.dp))
            if (isAdminState.value) {
                IconButton(onClick = { mVm.deleteMedia(media) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colors.red,
                        modifier = Modifier.width(30.dp)
                            .height(itemSize.dp)
                    )
                }
            } else {
                if (isDownloadedState != true) {
                    IconButton(onClick = { mVm.downloadMedia(media) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_download),
                            contentDescription = "Download",
                            tint = MaterialTheme.colors.red,
                            modifier = Modifier.width(30.dp)
                                .height(itemSize.dp)
                        )
                    }
                }
            }
            IconButton(onClick = {
                media.isFavoriteState = media.isFavorite == null || media.isFavorite == false
                mVm.setFavoriteMedia(media)
            }) {
                Icon(
                    Icons.Filled.Favorite,
                    "Favorite",
                    tint = if (media.isFavoriteState == true) {
                        MaterialTheme.colors.red
                    } else {
                        Color.Gray
                    },
                    modifier = Modifier.width(30.dp).height(itemSize.dp)
                )
            }
        }
    )
}
