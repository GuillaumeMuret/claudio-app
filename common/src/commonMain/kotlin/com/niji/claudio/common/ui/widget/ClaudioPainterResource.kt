package com.niji.claudio.common.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ClaudioPainterResource {
    @Composable
    fun get(claudioImageResource: ClaudioImageResource): Painter
}

data class ClaudioImageResource(
    val title: String,
    val extension: String = "xml"
)

object ClaudioPainterResourceId {
    val IC_BOLT = ClaudioImageResource("ic_bolt")
    val IC_CLEAR = ClaudioImageResource("ic_clear")
    val IC_FIRE = ClaudioImageResource("ic_fire")
    val IC_MUTE = ClaudioImageResource("ic_mute")
    val IC_VIBRATE = ClaudioImageResource("ic_vibrate", "png")
    val IC_PLAY = ClaudioImageResource("ic_play")
    val IC_REFRESH = ClaudioImageResource("ic_refresh")
    val IC_TOGGLE_DISPLAY = ClaudioImageResource("ic_toggle_display")
    val IC_VOLUME_DOWN = ClaudioImageResource("ic_volume_down")
    val IC_VOLUME_UP = ClaudioImageResource("ic_volume_up")
    val IC_DOWNLOAD = ClaudioImageResource("ic_download")
    val IC_MICRO = ClaudioImageResource("ic_micro", "png")
    val IC_SLEEP = ClaudioImageResource("ic_sleeping", "png")
    val IC_SMILE = ClaudioImageResource("ic_smile", "png")
}
