package com.example.futsalmanager.ui.home.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsalmanager.core.utils.Common
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.ui.theme.BrandGreen
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringBookingSheetContent(
    sheetState: SheetState,
    courts: List<Courts?>,
    onConfirm: (Courts, String) -> Unit,
    onDismiss: () -> Unit
) {
    // Current step state
    var currentStep by remember { mutableIntStateOf(1) }

    // Track selections across steps
    var selectedCourt by remember { mutableStateOf<Courts?>(null) }
    var selectedFreq by remember { mutableStateOf("Weekly") }


    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.surfaceVariant)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // Ensures content is above system nav buttons
                .padding(start = 20.dp, end = 20.dp, bottom = 32.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {
            item {
                // ---  Header with Icon ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                )
                {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EventRepeat,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Set Up Recurring Booking",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    "Reserve the same time slot automatically on a regular schedule.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                // ---. Static Stepper (Always Visible) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    StepCircle(number = "1", isActive = currentStep == 1, isDone = currentStep > 1)
                    HorizontalDivider(
                        modifier = Modifier
                            .width(40.dp)
                            .padding(horizontal = 8.dp),
                        color = if (currentStep > 1) BrandGreen else Color.LightGray
                    )
                    StepCircle(number = "2", isActive = currentStep == 2, isDone = currentStep > 2)
                    HorizontalDivider(
                        modifier = Modifier
                            .width(40.dp)
                            .padding(horizontal = 8.dp),
                        color = if (currentStep > 2) BrandGreen else Color.LightGray
                    )
                    StepCircle(number = "3", isActive = currentStep == 3, isDone = currentStep > 3)
                }

                // --- . Animated Content Transition ---
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        // If we are going to a higher step, slide in from right
                        if (targetState > initialState) {
                            (slideInHorizontally { it } + fadeIn()).togetherWith(
                                slideOutHorizontally { -it } + fadeOut())
                        } else {
                            // If we are going back, slide in from left
                            (slideInHorizontally { -it } + fadeIn()).togetherWith(
                                slideOutHorizontally { it } + fadeOut())
                        }.using(SizeTransform(clip = false))
                    },
                    label = "StepTransition"
                )
                { step ->
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        when (step) {
                            1 -> StepOneCourtSelection(
                                courts = courts,
                                onCourtSelected = {
                                    selectedCourt = it;
                                    currentStep = 2
                                }
                            )

                            2 -> StepTwoSchedule(
                                onNext = { currentStep = 3 },
                                onBack = { currentStep = 1 }
                            )

                            3 -> StepThreeReview(
                                onConfirm = { onConfirm(selectedCourt!!, selectedFreq) },
                                onBack = { currentStep = 2 }
                            )
                        }
                    }
                }
            }

        }
    }

}

@Composable
fun StepThreeReview(
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    var isSelected by remember { mutableStateOf(false) }
    var sessionCount by remember { mutableIntStateOf(1) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Frequency", fontWeight = FontWeight.Bold)
        Surface(
            onClick = {

            },
            shape = MaterialTheme.shapes.medium,
            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,

            border = BorderStroke(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )
        {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Weekend,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Text("Weekly", fontWeight = FontWeight.Bold)
                    Text("Every week on the same day", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
        Surface(
            onClick = {

            },
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,

            border = BorderStroke(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )
        {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Weekend,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Text("Bi-weekly", fontWeight = FontWeight.Bold)
                    Text("Every 2 weeks", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        SessionCounter(
            sessions = sessionCount,
            onSessionsChange = { sessionCount = it }
        )

        Spacer(Modifier.height(16.dp))
        Text(
            "Payment Method",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var isSelected by remember { mutableStateOf(false) }
                Surface(
                    onClick = {

                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,

                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(Modifier.weight(1f)) {
                            Text("Pay Per Session", fontWeight = FontWeight.Bold)
                            Text(
                                "Pay for each session individually",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Surface(
                    onClick = {

                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,

                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(Modifier.weight(1f)) {
                            Text("Monthly", fontWeight = FontWeight.Bold)
                            Text(
                                "Pay once per month",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Surface(
                    onClick = {

                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,

                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventRepeat,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(Modifier.weight(1f)) {
                            Text("Pay at Venue", fontWeight = FontWeight.Bold)
                            Text(
                                "Pay cash at the venue",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Select Card for Auto-Payment",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center

                    )
                }


            }
        }

        Spacer(Modifier.height(16.dp))
        StepNavigationButtons(
            onBack = onBack,
            onNext = onConfirm,
            nextButtonEnabled = true,
            nextButtonText = "Create Recurring Booking"
        )
    }

}

@Composable
fun SessionCounter(
    sessions: Int,
    onSessionsChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var sessionCount by remember { mutableIntStateOf(1) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Number of Sessions",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center, // Center the counter
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Decrease Button ---
            IconButton(
                onClick = { if (sessions > 1) onSessionsChange(sessions - 1) },
                enabled = sessions > 1
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveCircleOutline,
                    contentDescription = "Decrease",
                    tint = if (sessions > 1) BrandGreen else Color.Gray
                )
            }

            // --- Value Display ---
            Surface(
                modifier = Modifier.width(80.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
            ) {
                Text(
                    text = sessions.toString(),
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- Increase Button ---
            IconButton(
                onClick = { if (sessions < 10) onSessionsChange(sessions + 1) }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = "Increase",
                    tint = BrandGreen
                )
            }
        }

        AnimatedVisibility(
            visible = sessionCount > 1,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Text(
                text = "You are booking for $sessionCount consecutive weeks.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun StepOneCourtSelection(
    courts: List<Courts?>,
    onCourtSelected: (Courts) -> Unit
) {

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Select Court", fontWeight = FontWeight.Bold)

        var expanded by remember { mutableStateOf(false) }
        var selectedCourt by remember { mutableStateOf<Courts?>(null) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                .clickable { expanded = true }
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        selectedCourt?.name ?: "Choose a court",
                        color = if (selectedCourt == null) Color.Gray else Color.Black
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(Color.White)
                ) {
                    courts.forEach { court ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(court!!.name, fontWeight = FontWeight.Medium)
                                    Text(
                                        "Rs ${court.basePrice}/hr",
                                        color = BrandGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            onClick = {
                                selectedCourt = court
                                expanded = false
                                onCourtSelected(court!!)
                            }
                        )
                    }
                }

            }

        }
    }
}

@Composable
fun StepTwoSchedule(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var selectedDays by remember { mutableStateOf(setOf<DayOfWeek>()) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Which days weekly?",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        // Day Selection Chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 4
        ) {
            DayOfWeek.entries.forEach { day ->
                val isSelected = selectedDays.contains(day)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedDays = if (isSelected) selectedDays - day else selectedDays + day
                    },
                    label = { Text(day.getDisplayName(TextStyle.SHORT, Locale.getDefault())) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }

        Text(
            "Start Time?",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedCard(
                onClick = { expanded = true },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    1.dp,
                    if (selectedTime != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedTime?.format(timeFormatter) ?: "Select Time ",
                        color = if (selectedTime == null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (selectedTime != null) FontWeight.Bold else FontWeight.Normal
                    )
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = if (selectedTime != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Common.generateTimeSlots(1).forEach { time ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = time.format(timeFormatter),
                                fontWeight = FontWeight.Medium
                            )
                        },
                        onClick = {
                            selectedTime = time
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        StepNavigationButtons(
            onBack = onBack,
            onNext = onNext,
            nextButtonEnabled = selectedDays.isNotEmpty() && selectedTime != null,
            nextButtonText = "Next"
        )
    }
}

@Composable
fun StepCircle(
    number: String,
    isActive: Boolean,
    isDone: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                when {
                    isDone || isActive -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.surface
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isDone) {
            Icon(
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = "Done",
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Text(
                text = number,
                color = if (isActive) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@PreviewScreenSizes
@Composable
fun RecurringBookingSheetContentPreview() {
    StepTwoSchedule(onNext = {}, onBack = {})
    // RecurringBookingSheetContent(courts = emptyList(), onConfirm = { _, _ -> })
    //StepThreeReview(onConfirm = {}, onBack = {})
}

@Composable
fun StepNavigationButtons(
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    nextButtonEnabled: Boolean = true,
    backButtonText: String = "Back",
    nextButtonText: String = "Next",
    nextButtonColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Secondary Action
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.extraLarge,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = backButtonText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Primary Action
        Button(
            onClick = onNext,
            enabled = nextButtonEnabled,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.extraLarge,
            colors = ButtonDefaults.buttonColors(
                containerColor = nextButtonColor,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = nextButtonText,
                fontWeight = FontWeight.Bold,
                color = if (nextButtonEnabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}