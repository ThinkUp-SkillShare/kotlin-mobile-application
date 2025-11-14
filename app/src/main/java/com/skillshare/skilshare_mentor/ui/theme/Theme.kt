package com.skillshare.skilshare_mentor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,

    secondary = secondary,
    onSecondary = onSecondary,

    tertiary = tertiary,
    onTertiary = onTertiary,

    background = background,
    onBackground = onBackground,

    surface = surface,
    onSurface = onSurface,

    surfaceVariant = QuaternaryColor,
    onSurfaceVariant = PrimaryColor
)

@Composable
fun SkillShareTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}