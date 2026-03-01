package com.example.feature_home.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core_domain.domain.model.Courts
import com.example.core_ui.component.sharedComposables.BookingHeading
import com.example.feature_home.home.booking.BookingState
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BookingSelectionCard(
    state: BookingState,
    onCourtSelected: (Courts) -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- Header Section ---
        BookingHeading(
            "Select Court & Date",
            modifier = Modifier
                .fillMaxWidth(),
        )

        // --- Date Selection (Horizontal Strip) ---
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Available Dates",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(7) { i ->
                    val date = LocalDate.now().plusDays(i.toLong())
                    DateItem(
                        date = date,
                        isSelected = date == state.selectedDate,
                        onClick = {
                            onDateSelected(date)
                        }
                    )
                }
            }
        }

        // --- Court Selection (Visual Grid/List) ---
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Select Courts",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            state.courts.forEach { court ->
                val isSelected = state.selectedCourt == court

                Surface(
                    onClick = {
                        onCourtSelected(court!!)
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant,

                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        // Small icon representing court type
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SportsSoccer,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onPrimaryContainer,

                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                court!!.name,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "${court.type} Floor", fontSize = 12.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Rs ${court!!.basePrice}/hr",
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isToday = date == LocalDate.now()
    val dayName =
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).uppercase()
    val dayNumber = date.dayOfMonth.toString()

    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val subTextColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant

    val borderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(if (isSelected || isToday) 2.dp else 1.dp, borderColor),
        tonalElevation = if (isSelected) 4.dp else 1.dp,
        modifier = Modifier.width(68.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.labelSmall,
                color = subTextColor,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = dayNumber,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor
            )

            if (isToday) {
                Box(
                    Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
                )
            } else {
                Spacer(Modifier.size(6.dp))
            }
        }
    }
}