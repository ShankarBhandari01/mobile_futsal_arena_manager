import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.ui.component.ArenaCard
import com.example.futsalmanager.ui.component.ArenaShimmerItem
import com.example.futsalmanager.ui.component.FutsalDatePickerField
import com.example.futsalmanager.ui.home.HomeEffect
import com.example.futsalmanager.ui.home.HomeIntent
import com.example.futsalmanager.ui.home.HomeState
import com.example.futsalmanager.ui.home.navigation_drawer.FutsalDrawerSheet
import com.example.futsalmanager.ui.home.viewModels.HomeViewModel
import com.example.futsalmanager.ui.theme.BrandGreen
import com.example.futsalmanager.ui.theme.LightGreenBG
import com.example.futsalmanager.ui.theme.OrangeText
import com.example.futsalmanager.ui.theme.WarningYellowBG
import kotlinx.coroutines.launch


@Composable
fun FutsalHomeScreenRoute(
    snackbarHostState: SnackbarHostState,
) {
    val viewmodel = hiltViewModel<HomeViewModel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewmodel.effect.collect { e ->
            when (e) {
                is HomeEffect.ShowError -> snackbarHostState.showSnackbar(e.message)
                is HomeEffect.NavigateToMyBooking -> {
                    // Handle navigation
                }

                is HomeEffect.NavigateToMyProfile -> {
                    // Handle navigation
                }

                is HomeEffect.NavigateToMarketPlace -> {
                    // Handle navigation
                }

                is HomeEffect.NavigateToLogin -> {
                    // Handle navigation
                }
            }
        }
    }
    FutsalHomeScreen(
        state = state,
        onIntent = viewmodel::dispatch
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FutsalHomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState = HomeState(),
    onIntent: (HomeIntent) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            FutsalDrawerSheet(drawerState, scope)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = Color.Black,
                        titleContentColor = Color.Black,
                        actionIconContentColor = Color.Black
                    ),
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BrandGreen,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("F", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFD32F2F),
                            modifier = Modifier
                                .size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    "JP",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        )
        { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Trophy Icon Header
                    Surface(
                        shape = CircleShape,
                        color = LightGreenBG,
                        modifier = Modifier.size(100.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents, // Use a trophy icon
                            contentDescription = null,
                            tint = BrandGreen,
                            modifier = Modifier.padding(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Find & Book Futsal Courts",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Search arenas, view availability, and book your court instantly.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Search Fields
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Search arenas by name or city...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    FutsalDatePickerField()
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Toggle Buttons (Grid/Map)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(Color.White, RoundedCornerShape(6.dp))
                                .border(
                                    1.dp,
                                    Color.LightGray.copy(alpha = 0.3f),
                                    RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.GridView,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Map,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Location Warning Banner
                    Surface(
                        color = WarningYellowBG,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFF1B8)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.LocationOff,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Enable location to see nearby arenas first",
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Enable",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickable {

                                    }

                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            Modifier.weight(1f),
                            "34", "Arenas",
                            BrandGreen,
                            LightGreenBG
                        )
                        StatCard(
                            Modifier.weight(1f),
                            "24/7", "Booking",
                            BrandGreen,
                            LightGreenBG
                        )
                        StatCard(
                            Modifier.weight(1f),
                            "Instant",
                            "Confirm",
                            OrangeText,
                            Color(0xFFFFF7E6)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    if (state.isLoading && state.arenaList?.isEmpty() == true) {
                        //  shimmer items while loading
                        ArenaShimmerItem()
                    } else {
                        // Show actual data
                        ArenasListSection(arenas = state.arenaList)
                    }
                }
            }
        }
    }

}


@Composable
fun ArenasListSection(
    arenas: List<Arenas>? = emptyList()
) {
    if (arenas != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "All Arenas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            arenas.forEach { arena ->
                ArenaCard(
                    arena,
                    onItemClick = {

                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier,
    value: String,
    label: String,
    textColor: Color,
    bgColor: Color
) {
    Surface(
        modifier = modifier,
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
            Text(text = label, fontSize = 13.sp, color = Color.Gray)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun FutsalHomeScreenPreview() {
    FutsalHomeScreen(
        state = HomeState(),
        onIntent = {}
    )
}