package com.example.prac1.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    background = Neutral20Light,
    surface = Neutral10Light,
    onPrimary = Neutral40Light,
    onBackground = TextLight,
    onSurface = Neutral60Light,
    onTertiary = Neutral70Light,
    onPrimaryContainer = Neutral50Light,
    tertiary = ShadowLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    background = Neutral20Dark,
    surface = Neutral10Dark,
    onPrimary = Neutral40Dark,
    onBackground = TextDark,
    onSurface = Neutral60Dark,
    onTertiary = Neutral70Dark,
    onPrimaryContainer = Neutral50Dark,
    tertiary = ShadowDark
)

@Composable
fun FloweryTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}