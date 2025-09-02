package com.superapps.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = MainBlue,
        secondary = BlueVariant,
        background = Background,
        surface = Background,
        onPrimary = Background,
        onSecondary = Black,
        onBackground = Black,
        onSurface = Black,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = MainBlue,
        secondary = BlueVariant,
        background = Background,
        surface = Background,
        onPrimary = Background,
        onSecondary = Black,
        onBackground = Yellow,
        onSurface = Yellow,
    )

@Composable
fun SwordTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> LightColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
