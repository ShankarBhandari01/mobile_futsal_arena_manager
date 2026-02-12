package com.example.futsalmanager.ui.home.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.PaymentMethod
import com.example.futsalmanager.domain.model.Slot
import com.example.futsalmanager.domain.model.SlotStatus
import com.example.futsalmanager.ui.home.viewModels.BookingViewModel
import com.example.futsalmanager.ui.theme.BrandGreen
import com.example.futsalmanager.ui.theme.LightGreenBG
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArenaBookingScreen(
    state: BookingState = BookingState(),
    onIntent: (BookingIntent) -> Unit = {},
    onBackClick: () -> Unit
) {
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                ArenaHeaderCard(state)
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
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
                            color = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            item {
                RecurringBookingBanner()
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = LightGreenBG
                ) {
                    AsyncImage(
                        model = arena?.logoUrl ?: R.drawable.reshot,
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp)),
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
                            tint = BrandGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = arena?.city ?: "Location unknown",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
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
        color = LightGreenBG,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(14.dp), tint = BrandGreen)
            Spacer(Modifier.width(4.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = BrandGreen)
        }
    }
}


@Composable
fun RecurringBookingBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val stroke = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect
                        .dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
                drawRoundRect(
                    color = BrandGreen,
                    style = stroke,
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
            }
            .background(BrandGreen.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(16.dp)
            .clickable {

            }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.EventRepeat, contentDescription = null, tint = BrandGreen)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Recurring Booking", fontWeight = FontWeight.Bold, color = BrandGreen)
                Text("Reserve your spot weekly with a single tap.", fontSize = 12.sp)
            }
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
                color = BrandGreen,
                fontWeight = FontWeight.Bold
            )
        }

        // --- Date Selection (Horizontal Strip) ---
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Available Dates", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
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
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Select Courts", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

            state.courts.forEach { court ->
                val isSelected = state.selectedCourt == court

                Surface(
                    onClick = {
                        onCourtSelected(court!!)
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) LightGreenBG else Color.White,

                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) BrandGreen
                        else Color.LightGray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Small icon representing court type
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) BrandGreen else Color.LightGray.copy(alpha = 0.3f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SportsSoccer,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Color.Gray,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(Modifier.weight(1f)) {
                            Text(court!!.name, fontWeight = FontWeight.Bold)
                            Text("${court.type} Floor", fontSize = 12.sp, color = Color.Gray)
                        }

                        Text(
                            text = "Rs ${court!!.basePrice}/hr",
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSelected) BrandGreen else Color.Black
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

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) BrandGreen else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
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
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
            )
            Text(
                text = dayNumber,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.Black
            )
            // Dot indicator for "Today"
            if (date == LocalDate.now()) {
                Box(
                    Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.White else BrandGreen)
                )

            }
        }
    }
}


@Composable
fun AvailableSlotsSection(
    state: BookingState = BookingState(),
    onSlotSelected: (Slot) -> Unit = {}
) {

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // --- Header Section ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Available Slots - ${state.displayDate}",
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
        if (state.availableSlots.isEmpty() && !state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No slots available for this date.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        } else {

            TimeSlotGrid(
                slots = state.availableSlots,
                selectedSlot = state.selectedSlot,
                onSlotSelected = onSlotSelected
            )

            /*   LazyRow(
                   horizontalArrangement = Arrangement.spacedBy(12.dp),
                   contentPadding = PaddingValues(horizontal = 2.dp)
               ) {

                   items(state.availableSlots) { slot ->
                       TimeSlotCard(
                           slot = slot,
                           isSelected = state.selectedSlot?.start == slot.start,
                           status = when (slot.status) {
                               "AVAILABLE" -> SlotStatus.AVAILABLE.toString().uppercase()
                               "BOOKED" -> SlotStatus.BOOKED.toString().uppercase()
                               else -> SlotStatus.UNAVAILABLE.toString().uppercase()
                           },
                           onClick = {
                               onSlotSelected(slot)
                           }
                       )
                   }

               }*/

            StatusLegend()
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

    val isBooked = status == SlotStatus.BOOKED
    val isUnavailable = status == SlotStatus.UNAVAILABLE
    val isDisabled = isBooked || isUnavailable

    // Animations
    val animatedScale by animateFloatAsState(if (isSelected) 1.05f else 1f, label = "scale")
    val elevation by animateDpAsState(if (isSelected) 8.dp else 2.dp, label = "elevation")

    // Dynamic Colors based on Enum Status
    val contentColor = when {
        isBooked -> Color(0xFFE57373) // Soft Red
        isSelected -> BrandGreen
        else -> Color.Black
    }

    val containerColor by animateColorAsState(
        targetValue = when {
            isBooked -> Color(0xFFFFEBEE)
            isSelected -> BrandGreen.copy(alpha = 0.08f)
            else -> Color.White
        }, label = "bg"
    )

    Surface(
        onClick = { if (!isDisabled) onClick() },
        modifier = Modifier
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .size(width = 120.dp, height = 110.dp)
            .alpha(if (isUnavailable) 0.5f else 1f),
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        tonalElevation = elevation,
        shadowElevation = elevation,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = when {
                isBooked -> Color(0xFFEF9A9A)
                isSelected -> BrandGreen
                else -> Color.LightGray.copy(alpha = 0.3f)
            }
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // --- Pricing Badge / Status Badge ---
            val badgeText = if (isBooked) "BOOKED" else slot.pricingBadge
            if (badgeText.isNotEmpty() && !isUnavailable) {
                Surface(
                    color = if (isBooked) Color(0xFFEF5350) else BrandGreen,
                    shape = RoundedCornerShape(bottomStart = 12.dp, topEnd = 16.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = badgeText,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // --- Time Section ---
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (isBooked) Color(0xFFE57373) else if (isSelected) BrandGreen else Color.Gray
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = slot.start.toDisplayTime(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = contentColor
                        )
                    }
                    Text(
                        text = "to ${slot.end.toDisplayTime()}",
                        fontSize = 11.sp,
                        color = if (isBooked) Color(0xFFE57373).copy(alpha = 0.7f) else Color.Gray,
                        modifier = Modifier.padding(start = 18.dp)
                    )
                }

                // --- Price Section ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (isBooked) {
                        Text(
                            text = "Reserved",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE57373)
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Rs",
                                fontSize = 10.sp,
                                color = if (isSelected) BrandGreen else Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = slot.price,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = contentColor,
                                lineHeight = 18.sp
                            )
                        }
                    }
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