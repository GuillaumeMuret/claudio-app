package com.niji.claudio.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = DarkPrimary,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Color.Black,
    primaryVariant = Purple700,
    secondary = Teal200
)

val Colors.red: Color get() = if (isLight) Red300 else Red700
val Colors.orange: Color get() = if (isLight) Orange300 else Orange700
val Colors.green: Color get() = if (isLight) Green300 else Green700
val Colors.yellow: Color get() = if (isLight) Yellow300 else Yellow700
val Colors.pink: Color get() = if (isLight) Pink else Pink
val Colors.indigo: Color get() = if (isLight) Indigo300 else Indigo700
val Colors.teal: Color get() = if (isLight) Teal300 else Teal700

val Colors.mute: Color get() = if (isLight) Color.Black else Color.White
val Colors.mediaGridCards: Color get() = if (isLight) Color.Black else BlueGrey700
val Colors.toolbarBackground: Color get() = if (isLight) Color.Black else Color.Black

val Colors.claudioBackgroundStartColor: Color get() = if (isLight) Color.White else Grey700
val Colors.claudioBackgroundEndColor: Color get() = if (isLight) Color.White else Grey800

@Composable
fun ClaudioTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}