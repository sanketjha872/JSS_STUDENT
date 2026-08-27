package com.jhainusa.jss_student.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE5E5E5), //heading Color
    secondary = Color(0xFF1E1E20)  ,  //searchbarbg
    tertiary = Color(0xFFD1D5DB),
    onSurface = Color(0xFF9CA3AF),
    surface = Color(0xFF121212) ,
    background = Color(0xFF262626),
    onBackground = Color.White,
    primaryContainer = Color(0xFF4C4C4E),
    surfaceTint = Color(0xFF2E2E2E)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A1A1A), //heading Color
    secondary = Color(0xFFF1F1F3), //searchbarBg,
    tertiary = Color(0xFF4B5563),
    onSurface = Color(0xFF6B7280),
    surface = Color(0xFFF7F7F7),
    background = Color.White,
    onBackground = Color(0xFF262626),
    primaryContainer = Color(0xFFF3F4F6),
    surfaceTint = Color(0xFFF8F8F8)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun JSS_STUDENTTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            CompositionLocalProvider(
                LocalTextStyle provides TextStyle(
                    fontSize = TextUnit.Unspecified,
                    lineHeight = TextUnit.Unspecified,
                    letterSpacing = TextUnit.Unspecified,
                    fontWeight = null,
                    fontFamily = null
                )
            ) {
                content()
            }
        }
    )
}