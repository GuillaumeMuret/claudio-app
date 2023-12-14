package com.niji.claudio.common.ui.widget

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niji.claudio.common.ui.theme.green
import com.niji.claudio.common.ui.theme.red
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun ProgressIndicator(showOperationInProgress: MutableStateFlow<Boolean>) {
    val showNetworkOperationProgress: Boolean by showOperationInProgress.collectAsState()
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colors.red,
        targetValue = MaterialTheme.colors.green,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    if (showNetworkOperationProgress) {
        CircularProgressIndicator(modifier = Modifier.size(50.dp).padding(5.dp), color = color)
    }
}
