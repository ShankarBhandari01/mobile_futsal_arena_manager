package com.example.futsalmanager.ui.home.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.ui.theme.BrandGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringBookingSheetContent(
    courts: List<Courts?>,
    onConfirm: (Courts, String) -> Unit
) {
    // Current step state
    var currentStep by remember { mutableIntStateOf(1) }

    // Track selections across steps
    var selectedCourt by remember { mutableStateOf<Courts?>(null) }
    var selectedFreq by remember { mutableStateOf("Weekly") }

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray.copy(alpha = 0.5f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // Ensures content is above system nav buttons
                .padding(start = 20.dp, end = 20.dp, bottom = 32.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {
            // ---  Header with Icon ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EventRepeat,
                        contentDescription = null,
                        tint = BrandGreen,
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
            ) {
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
            ) { step ->
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
                            court = selectedCourt,
                            onConfirm = { onConfirm(selectedCourt!!, selectedFreq) },
                            onBack = { currentStep = 2 }
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun StepThreeReview(
    court: Courts?,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {

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
                        .fillMaxWidth(0.85f) // Adjusted for sheet width
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
fun StepTwoSchedule(onNext: () -> Unit, onBack: () -> Unit) {
    Text("Select Frequency", fontWeight = FontWeight.Bold)

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
            Text("Back")
        }
        Button(
            onClick = onNext,
            modifier = Modifier.weight(2f),
            colors = ButtonDefaults.buttonColors(BrandGreen)
        ) {
            Text("Next: Review")
        }
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
                    isDone || isActive -> BrandGreen
                    else -> Color(0xFFF1F3F4)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isDone) {
            Icon(
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = "Done",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Text(
                text = number,
                color = if (isActive) Color.White else Color.Gray,
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
    RecurringBookingSheetContent(courts = emptyList(), onConfirm = { _, _ -> })
}
