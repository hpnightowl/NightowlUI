package com.hpnightowl.systemui.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = ColorAccent,
    background = ColorSurfaceDark,
    surface = ColorSurfaceGlass,
    onPrimary = ColorTextPrimary,
    onBackground = ColorTextPrimary,
    onSurface = ColorTextPrimary
)

@Composable
fun SystemuiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

fun Modifier.glassmorphism(
    cornerRadius: Int = 24,
    borderWidth: Float = 1f
): Modifier = this
    .clip(RoundedCornerShape(cornerRadius.dp))
    .background(ColorSurfaceGlass)
    .border(
        width = borderWidth.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0x33FFFFFF),
                Color(0x05FFFFFF)
            )
        ),
        shape = RoundedCornerShape(cornerRadius.dp)
    )
