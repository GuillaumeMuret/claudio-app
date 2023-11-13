package com.niji.claudio.common.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

actual object ClaudioPainterResource {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    actual fun get(claudioImageResource: ClaudioImageResource): Painter =
        painterResource("drawable/${claudioImageResource.title}.${claudioImageResource.extension}")
}