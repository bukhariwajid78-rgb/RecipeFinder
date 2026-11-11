package com.choubapp.recipefinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Tomato,
    onPrimary = Color.White,
    primaryContainer = TomatoContainer,
    onPrimaryContainer = OnTomatoContainer,
    secondary = Olive,
    onSecondary = Color.White,
    secondaryContainer = OliveContainer,
    background = Cream,
    onBackground = Ink,
    surface = Cream,
    onSurface = Ink,
    surfaceVariant = SandVariant,
    onSurfaceVariant = Cocoa,
    outline = Color(0xFF85736D)
)

private val DarkColors = darkColorScheme(
    primary = TomatoDark,
    onPrimary = Color(0xFF5F1500),
    primaryContainer = TomatoContainerDark,
    onPrimaryContainer = TomatoContainer,
    secondary = OliveDark,
    background = Night,
    onBackground = OnNight,
    surface = Night,
    onSurface = OnNight,
    surfaceVariant = NightVariant,
    onSurfaceVariant = OnNightVariant,
    outline = Color(0xFFA08C85)
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp)
)

@Composable
fun RecipeFinderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
