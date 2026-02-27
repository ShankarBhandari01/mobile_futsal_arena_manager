package com.example.core_ui.component.theme


import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * 1. RAW COLOR TOKENS
 * Named based on their visual value
 */
val BrandGreen = Color(0xFF4CAF50)
val BrandGreenLight = Color(0xFFC8E6C9)
val BrandGreenDark = Color(0xFF2E7D32)

val WarningOrange = Color(0xFFD48806)
val WarningOrangeLight = Color(0xFFFFFBE6)

val ErrorRed = Color(0xFFBA1A1A)
val ErrorRedDark = Color(0xFF410002)

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF191C19) // M3 Neutral 10
val GreyVariant = Color(0xFF424940)

/**
 * 2. LIGHT COLOR SCHEME
 * Standard M3 Mapping
 */
val LightColorScheme = lightColorScheme(
    primary = BrandGreen,
    onPrimary = White,
    primaryContainer = BrandGreenLight,
    onPrimaryContainer = Color(0xFF002104),

    secondary = Color(0xFF52634F), // M3 Sage Green
    onSecondary = White,
    secondaryContainer = Color(0xFFD5E8CF),
    onSecondaryContainer = Color(0xFF101F10),

    tertiary = WarningOrange,
    onTertiary = White,
    tertiaryContainer = WarningOrangeLight,
    onTertiaryContainer = Color(0xFF281900),

    error = ErrorRed,
    onError = White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = ErrorRedDark,

    background = Color(0xFFFBFDF8),
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = Color(0xFFDEE5D8), // Unselected chips/toggles
    onSurfaceVariant = GreyVariant
)

/**
 * 3. DARK COLOR SCHEME
 * Uses desaturated tones for readability and eye comfort
 */
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784), // Lighter green for dark contrast
    onPrimary = Color(0xFF00390A),
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = BrandGreenLight,

    secondary = Color(0xFFB9CCB4),
    onSecondary = Color(0xFF253423),
    secondaryContainer = Color(0xFF3B4B38),
    onSecondaryContainer = Color(0xFFD5E8CF),

    tertiary = Color(0xFFFFB951), // Desaturated Warning Orange
    onTertiary = Color(0xFF452B00),
    tertiaryContainer = Color(0xFF633F00),
    onTertiaryContainer = Color(0xFFFFDDB3),

    background = Black,
    onBackground = Color(0xFFE2E3DD),
    surface = Black,
    onSurface = Color(0xFFE2E3DD),
    surfaceVariant = GreyVariant,
    onSurfaceVariant = Color(0xFFC2C9BD)
)
