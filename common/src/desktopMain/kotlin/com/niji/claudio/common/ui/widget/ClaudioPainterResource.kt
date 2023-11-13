package com.niji.claudio.common.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

actual object ClaudioPainterResource {

    private const val DRAWABLE_PATH = "drawable"

    @Composable
    actual fun get(claudioImageResource: ClaudioImageResource): Painter = painterResource(
        "${DRAWABLE_PATH}/${claudioImageResource.title}.${claudioImageResource.extension}"
    )
}
