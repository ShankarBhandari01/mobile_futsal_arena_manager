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
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.example.futsalmanager.R
import com.example.futsalmanager.core.utils.Common.toDisplayTime
import com.example.futsalmanager.core.utils.Common.toHourInt
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.Slot
import com.example.futsalmanager.domain.model.emum.PaymentMethod
import com.example.futsalmanager.domain.model.emum.SlotStatus
import com.example.futsalmanager.domain.model.emum.TimeSegment
import com.example.futsalmanager.ui.component.BannerState
import com.example.futsalmanager.ui.component.BookingHeading
import com.example.futsalmanager.ui.component.TopMessageBanner
import com.example.futsalmanager.ui.home.booking.recurringBooking.RecurringBookingSheetContent
import com.example.futsalmanager.ui.home.viewModels.BookingViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration.Builder(merchantDisplayName = "Futsal Manager")
            .allowsDelayedPaymentMethods(true)
            .build()
    )
}

@Composable
fun BookingScreenRoute(
    onBackClick: () -> Unit
) {

    var bannerState by remember { mutableStateOf<BannerState>(BannerState.Hidden) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = hiltViewModel<BookingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var customerConfig by remember { mutableStateOf<PaymentSheet.CustomerConfiguration?>(null) }
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }

    val paymentSheet = remember {
        PaymentSheet.Builder({
            bannerState = when (it) {
                is PaymentSheetResult.Completed -> {
                    BannerState.Success("Payment Successful! Booking confirmed.")
                }

                is PaymentSheetResult.Canceled -> {
                    BannerState.Hidden
                }

                is PaymentSheetResult.Failed -> {
                    BannerState.Error("Payment Failed: ${it.error.localizedMessage}")
                }
            }
        })
    }.build()


    LaunchedEffect(bannerState) {
        if (bannerState !is BannerState.Hidden) {
            delay(4000)
            bannerState = BannerState.Hidden
        }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is BookingEffect.ShowError -> {
                        bannerState = BannerState.Error(effect.message)
                    }

                    is BookingEffect.ShowPaymentDialog -> {
                        val payment = effect.paymentIntent
                        paymentIntentClientSecret = payment.paymentIntentResponseDTO.clientSecret

                        if (payment.user?.stripeCustomerId != null && payment.user.stripeCustomerId != "") {
                            customerConfig =
                                PaymentSheet.CustomerConfiguration.createWithCustomerSession(
                                    id = payment.user.stripeCustomerId,
                                    clientSecret = payment.paymentIntentResponseDTO.clientSecret
                                )
                        }
                        if (payment.arenas?.stripePublishableKey != null && payment.arenas?.stripePublishableKey != "") {
                            PaymentConfiguration.init(
                                context,
                                payment.arenas?.stripePublishableKey!!
                            )
                            if (customerConfig != null && paymentIntentClientSecret != null) {
                                presentPaymentSheet(
                                    paymentSheet,
                                    customerConfig!!,
                                    paymentIntentClientSecret!!
                                )
                            }
                        } else {
                            bannerState =
                                BannerState.Error("Online Payment not available at the moment. ")
                        }

                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ArenaBookingScreen(
            state = state,
            onIntent = viewModel::dispatch,
            onBackClick = onBackClick
        )

        TopMessageBanner(
            state = bannerState,
            onDismiss = { bannerState = BannerState.Hidden }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArenaBookingScreen(
    state: BookingState = BookingState(),
    onIntent: (BookingIntent) -> Unit = {},
    onBackClick: () -> Unit
) {
    var showRecurringDialog by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (showRecurringDialog) {
        RecurringBookingSheetContent(
            modalBottomSheetState,
            courts = state.courts,
            onDismiss = {
                showRecurringDialog = false
                scope.launch {
                    modalBottomSheetState.hide()
                }
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
        containerColor = MaterialTheme.colorScheme.background,
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
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
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
                        .fillMaxWidth(),
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
                        },
                        onPaymentButtonClick = {
                            onIntent(BookingIntent.MakePayment)
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
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(70.dp),
                    shape = MaterialTheme.shapes.medium
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
            .fillMaxWidth(),
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

@Composable
fun PaymentSection(
    state: BookingState,
    selectedMethod: PaymentMethod,
    onMethodSelected: (PaymentMethod) -> Unit,
    onPaymentButtonClick: () -> Unit = {}
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    val accentColor = MaterialTheme.colorScheme.primary


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(containerColor),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- Header Section ---
        BookingHeading(
            "Payment Method",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            step = 3,
            stepCount = 3
        )

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${state.displayStartTime} - ${state.displayEndTime}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = state.selectedCourt?.name ?: "Unknown Court",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "$${state.selectedCourt?.basePrice}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor
                )
            }
        }

        // Horizontal Selection with spacing
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val options = listOf(
                Triple("Online", "Instant", Icons.Default.CreditCard),
                Triple("Cash", "At venue", Icons.Default.Payments),
                Triple("Bank", "Manual", Icons.Default.AccountBalance)
            )

            val methods = PaymentMethod.entries.toTypedArray()

            methods.forEachIndexed { index, method ->
                val (title, sub, icon) = options[index]
                PaymentOptionCard(
                    modifier = Modifier.weight(1f),
                    title = title,
                    subtitle = sub,
                    icon = icon,
                    isSelected = selectedMethod == method,
                    onClick = { onMethodSelected(method) }
                )
            }
        }

        Button(
            onClick = {

                onPaymentButtonClick()

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            val buttonText = when (selectedMethod) {
                PaymentMethod.ONLINE -> "Reserve & Pay Online"
                PaymentMethod.CASH -> "Book Now, Pay Later"
                PaymentMethod.BANK -> "Generate Transfer Details"
            }
            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(12.dp))
            Text(
                buttonText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StatusLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Available -> Primary
        LegendItem(MaterialTheme.colorScheme.primaryContainer, "Available")

        // On Hold -> Tertiary
        LegendItem(MaterialTheme.colorScheme.tertiaryContainer, "On Hold")

        // Booked -> Error
        LegendItem(MaterialTheme.colorScheme.errorContainer, "Booked")

        // Unavailable -> Surface Variant
        LegendItem(MaterialTheme.colorScheme.surfaceVariant, "Unavailable")
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PaymentOptionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val secondaryContentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Surface(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        ),
        tonalElevation = if (isSelected) 0.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = secondaryContentColor,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
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

