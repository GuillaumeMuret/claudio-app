package com.niji.claudio.common.ui.widget

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable


@Composable
fun ClaudioDropdownMenuItem(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    DropdownMenuItem(
        onClick = onClick,
        content = content
    )
}
