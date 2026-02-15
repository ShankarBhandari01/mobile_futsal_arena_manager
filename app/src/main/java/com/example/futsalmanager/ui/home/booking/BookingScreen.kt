package com.example.futsalmanager.ui.home.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.futsalmanager.R
import com.example.futsalmanager.core.utils.Common.toDisplayTime
import com.example.futsalmanager.core.utils.Common.toHourInt
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.PaymentMethod
import com.example.futsalmanager.domain.model.Slot
import com.example.futsalmanager.domain.model.SlotStatus
import com.example.futsalmanager.domain.model.TimeSegment
import com.example.futsalmanager.ui.home.viewModels.BookingViewModel
import com.example.futsalmanager.ui.theme.BrandGreen
import java.time.LocalDate


@Composable
fun BookingScreenRoute(
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit
) {
    val viewModel = hiltViewModel<BookingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()


    ArenaBookingScreen(
        state = state,
        onIntent = viewModel::dispatch,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArenaBookingScreen(
    state: BookingState = BookingState(),
    onIntent: (BookingIntent) -> Unit = {},
    onBackClick: () -> Unit
) {
    var showRecurringDialog by remember { mutableStateOf(false) }

    if (showRecurringDialog) {
        RecurringBookingSheetContent(
            courts = state.courts,
            onConfirm = { court, frequency ->
                onIntent(BookingIntent.SetupRecurring(court, frequency))
                showRecurringDialog = false
            }
        )
    }
    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        snapshotFlow {
            Pair(state.selectedSlot, state.selectedCourt)
        }
            .collect { (slot, court) ->
                if (slot != null || court != null) {
                    val totalItems = listState.layoutInfo.totalItemsCount
                    if (totalItems > 0) {
                        listState.animateScrollToItem(totalItems - 1)
                    }
                }
            }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = { Text("Arena Details", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {

            item {
                ArenaHeaderCard(state)
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Column {
                        Text(
                            "Book a Court",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Text(
                            "Select your preferred court, date, and time slot at Sitapaila Sports Arena.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            item {
                RecurringBookingBanner(
                    onClick = {
                        showRecurringDialog = true
                    }
                )
            }

            item {
                BookingSelectionCard(
                    state = state,
                    onCourtSelected = {
                        onIntent(BookingIntent.SelectCourt(it))
                    },
                    onDateSelected = {
                        onIntent(BookingIntent.SelectDate(it))
                    }

                )
            }

            // 4. Time Slots and courts
            if (state.selectedCourt != null) {
                item {
                    AvailableSlotsSection(
                        state = state,
                        onSlotSelected = { slot ->
                            slot.isSelected = true
                            onIntent(BookingIntent.SelectSlot(slot))
                        }
                    )
                }
            }

            if (state.selectedSlot != null) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    PaymentSection(
                        state = state,
                        selectedMethod = state.selectedPaymentMethod,
                        onMethodSelected = { method ->
                            onIntent(BookingIntent.SelectPaymentMethod(method))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ArenaHeaderCard(state: BookingState) {
    val arena = state.arena

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(70.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    AsyncImage(
                        model = arena?.logoUrl ?: R.drawable.reshot,
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.reshot),
                        error = painterResource(R.drawable.reshot)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = arena?.name ?: "Loading Arena...",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = arena?.city ?: "Location unknown",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                itemVerticalAlignment = Alignment.CenterVertically
            ) {
                InfoBadge(
                    icon = Icons.Default.SportsSoccer,
                    label = "${arena?.courtCount ?: 0} Courts"
                )
                InfoBadge(
                    icon = Icons.Default.AccessTime,
                    label = arena?.timezone?.split("/")?.last() ?: "Kathmandu"
                )
                if (arena?.arenaType != null) {
                    InfoBadge(icon = Icons.Default.HistoryEdu, label = arena.arenaType)
                }
            }
        }
    }
}

@Composable
fun InfoBadge(icon: ImageVector, label: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(4.dp))
            Text(
                label, fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun RecurringBookingBanner(
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        val drawRound = MaterialTheme.colorScheme.primaryContainer
        Row(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    val stroke = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                    drawRoundRect(
                        color = drawRound,
                        style = stroke,
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EventRepeat,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Recurring Booking",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Reserve your spot weekly.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun BookingSelectionCard(
    state: BookingState,
    onCourtSelected: (Courts) -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- Header Section ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select Court & Date",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Step 1 of 3",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

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
    val dayName = date.dayOfWeek.name.take(3) // MON, TUE
    val dayNumber = date.dayOfMonth.toString()

    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    val subTextColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        // Tonal elevation adds a slight shadow/depth
        tonalElevation = if (isSelected) 4.dp else 0.dp,
        modifier = Modifier.width(65.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dayName,
                fontSize = 12.sp,
                color = subTextColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = dayNumber,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )

            Spacer(Modifier.height(4.dp))

            // Dot indicator for "Today"
            if (date == LocalDate.now()) {
                Box(
                    Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            } else {
                Spacer(Modifier.size(6.dp))
            }
        }
    }
}

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
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Section ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Available Slots",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Step 2 of 3",
                style = MaterialTheme.typography.bodySmall,
                color = BrandGreen,
                fontWeight = FontWeight.Bold
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(TimeSegment.entries.toTypedArray()) { segment ->
                val isSelected = selectedSegment == segment
                Surface(
                    onClick = { selectedSegment = segment },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) BrandGreen else Color.White,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) BrandGreen else Color.LightGray.copy(0.4f)
                    ),
                ) {
                    Text(
                        text = segment.label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) Color.White else Color.Gray
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
                Text("No slots available for this date.", color = Color.Gray)
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

            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
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
    val isDisabled = isBooked || isUnavailable


    // --- Colors ---
    val containerColor by animateColorAsState(
        targetValue = when {
            isBooked -> Color(0xFFFFF2F2)
            isSelected -> BrandGreen.copy(alpha = 0.12f)
            isAvailable -> BrandGreen.copy(alpha = 0.22f)
            else -> Color.White
        },
        label = "container"
    )

    val borderColor = when {
        isSelected -> BrandGreen
        isBooked -> Color(0xFFE57373)
        else -> Color.LightGray.copy(alpha = 0.25f)
    }

    val textColor = when {
        isBooked -> Color(0xFFD32F2F)
        isSelected -> BrandGreen
        else -> Color(0xFF222222)
    }

    Surface(
        onClick = { if (!isDisabled) onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isUnavailable) 0.45f else 1f),
        shape = RoundedCornerShape(22.dp),
        color = containerColor,
        border = if (isSelected)
            BorderStroke(1.5.dp, BrandGreen)
        else BorderStroke(1.dp, borderColor)
    ) {

        Box(Modifier.fillMaxSize()) {

            // -------- Badge (top-right) --------
            val badgeText = when {
                isBooked -> "BOOKED"
                slot.pricingBadge.isNotEmpty() -> slot.pricingBadge
                else -> ""
            }

            if (badgeText.isNotEmpty()) {
                Surface(
                    color = if (isBooked) Color(0xFFE57373) else BrandGreen,
                    shape = RoundedCornerShape(
                        bottomStart = 14.dp,
                        topEnd = 22.dp
                    ),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        badgeText,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // -------- Time --------
                Column {
                    Text(
                        text = slot.start.toDisplayTime(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor
                    )

                    Text(
                        text = "to ${slot.end.toDisplayTime()}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // -------- Price / Status --------
                if (isBooked) {
                    Text(
                        text = "Reserved",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD32F2F)
                    )
                } else {
                    Text(
                        text = "Rs ${slot.price}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = textColor
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

@Composable
fun PaymentSection(
    state: BookingState,
    selectedMethod: PaymentMethod,
    onMethodSelected: (PaymentMethod) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F3F4).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Section ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Payment Method",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Step 3 of 3",
                style = MaterialTheme.typography.bodySmall,
                color = BrandGreen,
                fontWeight = FontWeight.Bold
            )
        }
        // Summary Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "${state.displayStartTime} - ${state.displayEndTime}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    state.selectedCourt?.name ?: "",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Text(
                "$${state.selectedCourt?.basePrice}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        }

        Text(
            state.selectedPaymentMethod.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentOptionCard(
                modifier = Modifier.weight(1f),
                title = "Pay Online",
                subtitle = "Instant confirmation",
                icon = Icons.Default.CreditCard,
                isSelected = selectedMethod == PaymentMethod.ONLINE,
                onClick = { onMethodSelected(PaymentMethod.ONLINE) }
            )
            PaymentOptionCard(
                modifier = Modifier.weight(1f),
                title = "Pay Cash",
                subtitle = "Pay at venue",
                icon = Icons.Default.Payments,
                isSelected = selectedMethod == PaymentMethod.CASH,
                onClick = { onMethodSelected(PaymentMethod.CASH) }
            )
            PaymentOptionCard(
                modifier = Modifier.weight(1f),
                title = "Bank Transfer",
                subtitle = "Manual verification",
                icon = Icons.Default.AccountBalance,
                isSelected = selectedMethod == PaymentMethod.BANK,
                onClick = { onMethodSelected(PaymentMethod.BANK) }
            )
        }

        // Final Action Button
        Button(
            onClick = { /* Process Booking */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
        ) {
            Icon(Icons.Default.CreditCard, null)
            Spacer(Modifier.width(8.dp))
            Text("Reserve & Pay Online", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StatusLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LegendItem(Color(0xFFC8E6C9), "Available")
        LegendItem(Color(0xFFFFF9C4), "On Hold")
        LegendItem(Color(0xFFFFCDD2), "Booked")
        LegendItem(Color(0xFFE0E0E0), "Unavailable")
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun PaymentOptionCard(
    modifier: Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color.White else Color.Transparent,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) BrandGreen else Color.LightGray.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = if (isSelected) Color.Black else Color.Gray)
            Spacer(Modifier.height(8.dp))
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Text(
                subtitle,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@PreviewScreenSizes
@Composable
fun ArenaBookingScreenPreview() {
    ArenaBookingScreen(onBackClick = {})
}

