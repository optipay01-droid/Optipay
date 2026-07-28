package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SleekColorScheme = lightColorScheme(
    primary = SleekPrimary,
    onPrimary = Color.White,
    primaryContainer = SleekRoseContainer,
    onPrimaryContainer = SleekDarkTerracotta,
    secondary = SleekPrimaryDark,
    onSecondary = Color.White,
    secondaryContainer = SleekRoseContainer,
    onSecondaryContainer = SleekDarkTerracotta,
    tertiary = EmeraldSuccess,
    background = SleekCanvasBg,
    onBackground = SleekTextMain,
    surface = SleekCardBg,
    onSurface = SleekTextMain,
    surfaceVariant = SleekCardBgAlt,
    onSurfaceVariant = SleekTextMuted,
    outline = SleekBorder
)

@Composable
fun WatchAndEarnTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SleekColorScheme,
        typography = Typography,
        content = content
    )
}

