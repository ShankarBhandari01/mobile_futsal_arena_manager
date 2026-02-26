package com.example.futsalmanager.ui.home.booking

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SportsSoccer
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
import com.example.futsalmanager.core.ui.states.BannerState
import com.example.futsalmanager.core.ui.sharedComposables.TopMessageBanner
import com.example.futsalmanager.ui.component.AvailableSlotsSection
import com.example.futsalmanager.ui.component.BookingSelectionCard
import com.example.futsalmanager.ui.component.PaymentSection
import com.example.futsalmanager.ui.home.booking.recurringBooking.RecurringBookingSheetContent
import com.example.futsalmanager.ui.home.viewModels.BookingViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    val googlePayConfiguration = PaymentSheet.GooglePayConfiguration(
        environment = PaymentSheet.GooglePayConfiguration.Environment.Test,
        countryCode = "NP",
        currencyCode = "NPR"
    )
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration.Builder(merchantDisplayName = "Futsal Manager")
            .googlePay(googlePayConfiguration)
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


@RequiresApi(Build.VERSION_CODES.O)
@PreviewScreenSizes
@Composable
fun ArenaBookingScreenPreview() {
    ArenaBookingScreen(onBackClick = {})
}

