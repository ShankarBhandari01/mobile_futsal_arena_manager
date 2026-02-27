package com.example.core_ui.component.theme


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    // Used for DropdownMenus, Tooltips, and small components
    extraSmall = RoundedCornerShape(4.dp),

    // Used for Chips and smaller buttons
    small = RoundedCornerShape(8.dp),

    // Used for Cards and Dialogs (like your Booking cards)
    medium = RoundedCornerShape(12.dp),

    // Used for Bottom Sheets and large containers
    large = RoundedCornerShape(24.dp),

    // Used for Buttons and search bars (Pill shape)
    extraLarge = RoundedCornerShape(32.dp)
)