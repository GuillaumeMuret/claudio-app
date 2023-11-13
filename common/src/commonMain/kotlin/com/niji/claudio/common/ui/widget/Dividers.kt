package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LineDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 0.dp),
        thickness = 1.dp,
        color = MaterialTheme.colors.primary
    )
}

@Composable
fun CommonDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 30.dp),
        thickness = 1.dp,
        color = MaterialTheme.colors.primary,
    )
}

@Composable
fun RemoteDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 55.dp),
        thickness = 1.dp,
        color = MaterialTheme.colors.primary,
    )
}