package com.example.feature_home.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core_data.data.model.Slot
import com.example.core_data.data.model.emum.SlotStatus
import com.example.core_data.data.model.emum.TimeSegment
import com.example.core_ui.component.sharedComposables.BookingHeading
import com.example.core_uitls.utils.Common.toDisplayTime
import com.example.core_uitls.utils.Common.toHourInt
import com.example.feature_home.home.booking.BookingState

@Composable
fun AvailableSlotsSection(
    state: BookingState = BookingState(),
    onSlotSelected: (Slot) -> Unit = {}
) {
    var selectedSegment by remember { mutableStateOf(TimeSegment.ALL) }

    val filteredSlots = remember(state.availableSlots, selectedSegment) {
        state.availableSlots.filter { slot ->
            val hour = slot.start.toHourInt()
            when (selectedSegment) {
                TimeSegment.MORNING -> hour in 5..11
                TimeSegment.AFTERNOON -> hour in 12..16
                TimeSegment.EVENING -> hour in 17..23
                TimeSegment.ALL -> true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Section ---
        BookingHeading(
            "Available Slots",
            modifier = Modifier
                .fillMaxWidth(),
            step = 2,
            stepCount = 3
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(TimeSegment.entries.toTypedArray()) { segment ->
                val isSelected = selectedSegment == segment

                val containerColor = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainerLow
                }
                val contentColor = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }

                val borderColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }

                Surface(
                    onClick = { selectedSegment = segment },
                    shape = RoundedCornerShape(12.dp),
                    color = containerColor,
                    border = BorderStroke(width = 1.dp, color = borderColor),
                ) {
                    Text(
                        text = segment.label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor
                    )
                }
            }
        }

        // --- 3. Empty State vs Grid ---
        if (state.availableSlots.isEmpty() && !state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No slots available for this date.",
                    color = Color.Gray
                )
            }
        } else if (filteredSlots.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No slots in the ${selectedSegment.label} period.", color = Color.Gray)
            }
        } else {
            TimeSlotGrid(
                slots = filteredSlots,
                selectedSlot = state.selectedSlot,
                onSlotSelected = onSlotSelected
            )
            Box {
                StatusLegend()
            }
        }
    }
}

@Composable
fun TimeSlotCard(
    slot: Slot,
    isSelected: Boolean = false,
    status: SlotStatus = SlotStatus.AVAILABLE,
    onClick: () -> Unit
) {
    val isAvailable = status == SlotStatus.AVAILABLE
    val isBooked = status == SlotStatus.BOOKED
    val isUnavailable = status == SlotStatus.UNAVAILABLE
    val isOnHold = status == SlotStatus.ON_HOLD
    val isDisabled = isBooked || isUnavailable

    val containerColor by animateColorAsState(
        targetValue = when {
            isBooked -> MaterialTheme.colorScheme.errorContainer
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            isAvailable -> MaterialTheme.colorScheme.surfaceContainerHigh
            isOnHold -> MaterialTheme.colorScheme.tertiaryContainer
            else -> MaterialTheme.colorScheme.surface
        },
        label = "containerColor"
    )

    val contentColor = when {
        isBooked -> MaterialTheme.colorScheme.onErrorContainer
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    val borderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isBooked -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Surface(
        onClick = { if (isAvailable) onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isUnavailable) 0.38f else 1f),
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        tonalElevation = if (isAvailable && !isSelected) 2.dp else 0.dp
    ) {
        Box(Modifier.fillMaxSize()) {

            // --- Badge Logic ---
            val badgeText = if (isBooked) "BOOKED" else slot.pricingBadge
            if (badgeText.isNotEmpty()) {
                Surface(
                    color = if (isBooked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    contentColor = if (isBooked) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = slot.start.toDisplayTime(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = contentColor
                    )
                    Text(
                        text = "to ${slot.end.toDisplayTime()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondaryTextColor
                    )
                }

                if (isBooked) {
                    Text(
                        text = "Reserved",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(
                        text = "Rs ${slot.price}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = contentColor
                    )
                }
            }
        }
    }
}


@Composable
fun TimeSlotGrid(
    slots: List<Slot>,
    selectedSlot: Slot?,
    onSlotSelected: (Slot) -> Unit
) {

    val rows = slots.chunked(3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rows.forEach { rowSlots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowSlots.forEach { slot ->
                    Box(modifier = Modifier.weight(1f)) {
                        TimeSlotCard(
                            slot = slot,
                            isSelected = slot == selectedSlot,
                            status = slot.slotStatus,
                            onClick = { onSlotSelected(slot) }
                        )
                    }
                }
                if (rowSlots.size < 3) {
                    repeat(3 - rowSlots.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}