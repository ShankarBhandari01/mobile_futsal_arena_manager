package com.example.futsalmanager.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val green = Color(0xFF22C55E)
val lightGreen = Color(0xFFE8F5E9)
val white = Color(0xFFFFFFFF)
val black = Color(0xFF000000)


//  the brand colors
val LightGreenBG = Color(0xFFF1F8F1)
val BrandGreen = Color(0xFF4CAF50)
val WarningYellowBG = Color(0xFFFFFBE6)
val OrangeText = Color(0xFFD48806)


// Light Palette
val LightColorScheme = lightColorScheme(
    primary = BrandGreen, //  Brand Green
    onPrimary = Color.White,
    surface = Color(0xFFFFFFFF), // White background for cards/screens
    onSurface = Color(0xFF1C1B1F), // Dark text for light mode
    secondaryContainer = Color(0xFFF1F8F1), // The LightGreenBG for selected items
    surfaceVariant = Color(0xFFF5F5F5) // For the toggle background
)

// Dark Palette
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784), // A slightly lighter green for dark mode readability
    onPrimary = Color(0xFF00390A),
    surface = Color(0xFF121212), // Deep dark background
    onSurface = Color(0xFFE6E1E5), // Light text for dark mode
    secondaryContainer = Color(0xFF233223), // Subdued green for selection in dark mode
    surfaceVariant = Color(0xFF49454F)
)