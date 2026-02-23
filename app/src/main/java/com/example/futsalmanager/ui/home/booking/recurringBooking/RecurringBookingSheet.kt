package com.example.futsalmanager.ui.home.booking.recurringBooking

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.futsalmanager.core.utils.Common
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.Frequency
import com.example.futsalmanager.domain.model.PaymentStyle
import com.example.futsalmanager.ui.home.viewModels.RecurringBookingViewModel
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringBookingSheetContent(
    sheetState: SheetState,
    courts: List<Courts?>,
    onDismiss: () -> Unit
) {
    // viewmodel
    val viewModel = hiltViewModel<RecurringBookingViewModel>()
    // state
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Current step state
    var currentStep by remember { mutableIntStateOf(1) }


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
                Spacer(Modifier.height(8.dp))
                Text(
                    "Reserve the same time slot automatically on a regular schedule.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        color = if (currentStep > 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                    StepCircle(number = "2", isActive = currentStep == 2, isDone = currentStep > 2)
                    HorizontalDivider(
                        modifier = Modifier
                            .width(40.dp)
                            .padding(horizontal = 8.dp),
                        color = if (currentStep > 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                    StepCircle(number = "3", isActive = currentStep == 3, isDone = currentStep > 3)
                }

                // --- . Animated Content Transition ---
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { it } + fadeIn()).togetherWith(
                                slideOutHorizontally { -it } + fadeOut())
                        } else {
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
                                state = state,
                                onIntent = viewModel::dispatch,
                                onCourtSelected = {
                                    currentStep = 2
                                }
                            )

                            2 -> StepTwoSchedule(
                                state = state,
                                onIntent = viewModel::dispatch,
                                onNext = { currentStep = 3 },
                                onBack = { currentStep = 1 }
                            )

                            3 -> StepThreeReview(
                                state = state,
                                onIntent = viewModel::dispatch,
                                onConfirm = {
                                },
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
    state: RecurringBookingState,
    onIntent: (RecurringBookingIntent) -> Unit,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // --- Frequency Section ---
        Text(
            "Frequency",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        val frequencyOptions = listOf(
            Triple("Weekly", "Every week on the same day", Icons.Default.Weekend),
            Triple("Bi-Weekly", "Every 2 weeks", Icons.Default.EventRepeat)
        )

        // Dynamically generating frequency Options from an enum/list
        Frequency.entries.forEachIndexed { index, frequency ->
            val (title, sub, icon) = frequencyOptions[index]
            SelectableOptionCard(
                title = title,
                subtitle = sub,
                icon = icon,
                isSelected = state.frequency == frequency,
                onClick = { onIntent(RecurringBookingIntent.UpdateFrequency(frequency)) }
            )
        }

        Spacer(Modifier.height(8.dp))
        // --- Session Counter ---
        SessionCounter(
            sessions = state.sessionCount,
            onSessionsChange = { onIntent(RecurringBookingIntent.UpdateSessionCount(it)) }
        )

        Spacer(Modifier.height(8.dp))

        // --- Payment Section ---
        Text(
            "Payment Method",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        val paymentOptions = listOf(
            Triple("Pay Per Session", "Individually", Icons.Default.Payment),
            Triple("Monthly", "Once per month", Icons.Default.CalendarMonth),
            Triple("Pay at Venue", "Cash at venue", Icons.Default.Storefront)
        )

        // Dynamically generating payment options from an enum/list
        PaymentStyle.entries.forEachIndexed { index, method ->
            val (title, sub, icon) = paymentOptions[index]
            SelectableOptionCard(
                title = title,
                subtitle = sub,
                icon = icon,
                isSelected = state.selectedPaymentStyle == method,
                onClick = { onIntent(RecurringBookingIntent.UpdatePaymentMethod(method)) }
            )
        }
        // --- Card selection for Auto-Payment ---
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            elevation = CardDefaults.cardElevation(2.dp)
        )
        {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Header Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Select Card for Auto-Payment",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Inner Empty State Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(0.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "No saved cards yet",
                            style = MaterialTheme.typography.titleMedium
                        )

                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Card"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Card")
                        }
                    }
                }

                // Info Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "A saved card is required for automatic payments",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        // --- Booking Summary
        BookingSummaryCard(
            state = state
        )
        // --- Footer Navigation ---
        Spacer(Modifier.weight(1f)) // Push buttons to bottom
        StepNavigationButtons(
            onBack = onBack,
            onNext = onConfirm,
            nextButtonEnabled = state.isValid(),
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
                    tint = if (sessions > 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // --- Value Display ---
            Surface(
                modifier = Modifier.width(80.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
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
                    tint = MaterialTheme.colorScheme.primary
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SelectableOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    val iconContainerColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val iconTint =
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = iconContainerColor,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StepOneCourtSelection(
    state: RecurringBookingState,
    onIntent: (RecurringBookingIntent) -> Unit,
    courts: List<Courts?>,
    onCourtSelected: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Select Court", fontWeight = FontWeight.Bold)
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12.dp))
                .clickable { expanded = true }
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        state.selectedCourt?.name ?: "Choose a court",
                        color = if (state.selectedCourt == null)
                            MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.onBackground
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    courts.forEach { court ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        court!!.name,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "Rs ${court.basePrice}/hr",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            onClick = {
                                onIntent(RecurringBookingIntent.OnCourtSelected(court))
                                expanded = false
                                onCourtSelected()
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
    state: RecurringBookingState,
    onIntent: (RecurringBookingIntent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
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
                val isSelected = state.selectedDay == day
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        onIntent(RecurringBookingIntent.OnDaySelected(day))
                    },
                    label = {
                        Text(day.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
                    },
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
                    if (state.selectedTime != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = state.selectedTime?.format(timeFormatter) ?: "Select Time ",
                        color = if (state.selectedTime == null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (state.selectedTime != null) FontWeight.Bold else FontWeight.Normal
                    )
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = if (state.selectedTime != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
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
                            onIntent(RecurringBookingIntent.OnTimeSelected(time))
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
            nextButtonEnabled = state.isScreenTwoValid(),
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


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@PreviewScreenSizes
@Composable
fun RecurringBookingSheetContentPreview() {
    /*StepTwoSchedule(
        state = RecurringBookingState(),
         onIntent = {},
          onNext = {}, onBack = {}
      )*/

    /*    RecurringBookingSheetContent(
            courts = emptyList(),
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismiss = {}
        )*/

    StepThreeReview(
        onConfirm = {},
        onBack = {},
        state = RecurringBookingState(),
        onIntent = {}
    )
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

@Composable
fun BookingSummaryCard(
    state: RecurringBookingState,
) {
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )

                Text(
                    text = "Booking Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            val scheduleTime = state.selectedTime?.format(timeFormatter)
            val selectedDay =
                state.selectedDay?.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val estTotal = state.sessionCount * state.selectedCourt!!.basePrice.toDouble()

            // Details
            SummaryRow("Court", state.selectedCourt.name)
            SummaryRow("Schedule", "$selectedDay @ $scheduleTime")
            SummaryRow("Frequency", state.frequency?.displayName!!)
            SummaryRow("Payment", state.selectedPaymentStyle?.displayName!!)
            SummaryRow("Total Sessions", state.sessionCount.toString())

            HorizontalDivider(
                Modifier,
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Estimated Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Est. Total",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "$${String.format("%.2f", estTotal)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Info Note
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = "Price may vary based on peak hours and special rates. You'll be charged per session.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}