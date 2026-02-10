package com.example.futsalmanager.ui.home.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    onCourtSelected = {

                    }
                )
            }

            // 4. Time Slots
            item {
                AvailableSlotsSection()
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
fun Badge(name: String) {
    Surface(
        color = Color(0xFFF1F3F4), // Light gray background
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Public,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = name,
                color = Color.DarkGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun HorizontalDateSelector() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(7) { day ->
            Column(
                modifier = Modifier
                    .width(60.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Mon", fontSize = 12.sp, color = Color.Gray)
                Text("${9 + day}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingSelectionCard(
    state: BookingState = BookingState(),
    onCourtSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCourt by remember { mutableStateOf("Court 1") }
    val courts = listOf("Court 1", "Court 2", "Court A", "Court B")

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Select Court & Date", fontWeight = FontWeight.Bold)

        // Court Selector (Dropdown)
        OutlinedCard(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(selectedCourt)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                courts.forEach { court ->
                    DropdownMenuItem(
                        text = { Text(court) },
                        onClick = {
                            selectedCourt = court
                            expanded = false
                            onCourtSelected(court)
                        }
                    )
                }
            }
        }

        // Date Selector (Horizontal Scroll)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(14) { i ->
                val date = LocalDate.now().plusDays(i.toLong())
                DateItem(date = date, isSelected = i == 0)

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateItem(date: LocalDate, isSelected: Boolean) {
    val bgColor = if (isSelected) BrandGreen else Color.White
    val textColor = if (isSelected) Color.White else Color.Black

    Card(
        modifier = Modifier.width(60.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = if (!isSelected) BorderStroke(1.dp, Color.LightGray) else null
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                date.dayOfWeek.name.take(3),
                fontSize = 11.sp,
                color = textColor.copy(alpha = 0.7f)
            )
            Text(
                date.dayOfMonth.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = textColor
            )
        }
    }
}

@Composable
fun AvailableSlotsSection(
    state: BookingState = BookingState(),
) {
    val morningSlots = listOf("06:00 AM", "07:00 AM", "08:00 AM", "09:00 AM")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Available Slots", fontWeight = FontWeight.Bold)

        Text("Morning", color = Color.Gray, fontSize = 14.sp)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            morningSlots.forEach { time ->
                TimeSlotChip(time)
            }
        }
    }
}

@Composable
fun TimeSlotChip(time: String) {
    Surface(
        modifier = Modifier.width(100.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        color = Color.White
    ) {
        Text(
            text = time,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center,
            fontSize = 13.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@PreviewScreenSizes
@Composable
fun ArenaBookingScreenPreview() {
    ArenaBookingScreen(onBackClick = {})
}