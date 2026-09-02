package com.example.calculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors =
    darkColorScheme(
        background = Color(0xFF000000),
        surface = Color(0xFF121212),
        onBackground = Color.White,
        onSurface = Color.White,
    )

private val LightColors =
    lightColorScheme(
        background = Color(0xFFF3F4F6),
        surface = Color.White,
        onBackground = Color.Black,
        onSurface = Color.Black,
    )

@Composable
fun CalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content,
    )
}
