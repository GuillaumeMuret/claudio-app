package com.niji.claudio.common.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.niji.claudio.common.R

actual object ClaudioPainterResource {
    @Composable
    actual fun get(claudioImageResource: ClaudioImageResource): Painter = painterResource(
        when (claudioImageResource.title) {
            ClaudioPainterResourceId.IC_BOLT.title -> R.drawable.ic_bolt
            ClaudioPainterResourceId.IC_CLEAR.title -> R.drawable.ic_clear
            ClaudioPainterResourceId.IC_DOWNLOAD.title -> R.drawable.ic_download
            ClaudioPainterResourceId.IC_FIRE.title -> R.drawable.ic_fire
            ClaudioPainterResourceId.IC_MUTE.title -> R.drawable.ic_mute
            ClaudioPainterResourceId.IC_VIBRATE.title -> R.drawable.ic_vibrate
            ClaudioPainterResourceId.IC_PLAY.title -> R.drawable.ic_play
            ClaudioPainterResourceId.IC_REFRESH.title -> R.drawable.ic_refresh
            ClaudioPainterResourceId.IC_TOGGLE_DISPLAY.title -> R.drawable.ic_toggle_display
            ClaudioPainterResourceId.IC_VOLUME_DOWN.title -> R.drawable.ic_volume_down
            ClaudioPainterResourceId.IC_VOLUME_UP.title -> R.drawable.ic_volume_up
            ClaudioPainterResourceId.IC_SMILE.title -> R.drawable.ic_smile
            ClaudioPainterResourceId.IC_SLEEP.title -> R.drawable.ic_sleeping
            ClaudioPainterResourceId.IC_MICRO.title -> R.drawable.ic_micro
            else -> R.drawable.ic_clear
        }
    )
}
