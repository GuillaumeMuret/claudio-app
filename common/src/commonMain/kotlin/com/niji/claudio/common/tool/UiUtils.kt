package com.niji.claudio.common.tool

import com.niji.claudio.common.ui.state.AppViewState

object UiUtils {
    private const val KYLO_BYTE = 1024
    private const val MEDIA_DISPLAY_COLUMN = "MediaDisplayColumn"
    private const val MEDIA_DISPLAY_GRID = "MediaDisplayGrid"

    fun getFriendlySize(size: Int): String {
        return if (size / KYLO_BYTE > 1000) {
            if (size / (KYLO_BYTE * KYLO_BYTE) > 1000) {
                "" + (size / (KYLO_BYTE * KYLO_BYTE * KYLO_BYTE)) + "Gb"
            } else {
                "" + (size / (KYLO_BYTE * KYLO_BYTE)) + "Mb"
            }
        } else {
            "" + (size / KYLO_BYTE) + "Kb"
        }
    }

    fun getMediaStateClass(state: String): AppViewState {
        return if (state == MEDIA_DISPLAY_COLUMN) {
            AppViewState.MediaDisplayColumn
        } else {
            AppViewState.MediaDisplayGrid
        }
    }

    fun getMediaStateString(state: AppViewState): String {
        return if (state is AppViewState.MediaDisplayColumn) {
            MEDIA_DISPLAY_COLUMN
        } else {
            MEDIA_DISPLAY_GRID
        }
    }

    fun isCorrectAddMediaField(str: String): Boolean {
        return str.length >= 2
    }
}