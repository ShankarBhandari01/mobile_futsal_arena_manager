import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.futsalmanager.core.utils.LocationUtils.launchLocationSettingsResolution
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.ui.component.ArenaCard
import com.example.futsalmanager.ui.component.ArenaShimmerItem
import com.example.futsalmanager.ui.component.EmptyStateComponent
import com.example.futsalmanager.ui.component.FutsalDatePickerField
import com.example.futsalmanager.ui.component.GenericSegmentedToggle
import com.example.futsalmanager.ui.component.LocationSuccessBanner
import com.example.futsalmanager.ui.component.LocationWarningBanner
import com.example.futsalmanager.ui.component.LogoutConfirmationDialog
import com.example.futsalmanager.ui.home.HomeEffect
import com.example.futsalmanager.ui.home.HomeIntent
import com.example.futsalmanager.ui.home.HomeState
import com.example.futsalmanager.ui.home.ViewMode
import com.example.futsalmanager.ui.home.navigation_drawer.FutsalDrawerSheet
import com.example.futsalmanager.ui.home.viewModels.HomeViewModel
import com.example.futsalmanager.ui.theme.BrandGreen
import com.example.futsalmanager.ui.theme.LightGreenBG
import com.example.futsalmanager.ui.theme.OrangeText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FutsalHomeScreenRoute(
    snackbarHostState: SnackbarHostState,
    onLogout: () -> Unit
) {
    val viewmodel = hiltViewModel<HomeViewModel>()
    val context = LocalContext.current
    val state by viewmodel.state.collectAsStateWithLifecycle()


    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { _ -> }

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }


    LaunchedEffect(viewmodel.effect) {
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
                    onLogout()
                }

                is HomeEffect.NavigateToLocationSettings -> {
                    launchLocationSettingsResolution(context, settingsLauncher)
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
    val haptics = LocalHapticFeedback.current
    val pullToRefreshState = rememberPullToRefreshState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    if (state.showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                onIntent(HomeIntent.ConfirmLogout)
            },
            onDismiss = {
                onIntent(HomeIntent.DismissLogoutDialog)
            }
        )
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            FutsalDrawerSheet(drawerState, scope, onIntent)
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
                    ), title = {
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
                    }, navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed)
                                    drawerState.open() else
                                    drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }, actions = {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFD32F2F),
                            modifier = Modifier.size(36.dp)
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
            })
        { padding ->
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = state.isLoading,
                onRefresh = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onIntent(HomeIntent.Refresh)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        state = pullToRefreshState,
                        isRefreshing = state.isLoading,
                        modifier = Modifier.align(Alignment.TopCenter),
                        containerColor = Color.White,
                        color = BrandGreen
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 24.dp)
                )
                {
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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
                                modifier = Modifier.padding(vertical = 12.dp),

                                )
                        }
                    }
                    item {
                        // Search Fields
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            OutlinedTextField(
                                value = state.search,
                                onValueChange = {
                                    onIntent(HomeIntent.SearchChanged(it))
                                },
                                placeholder = { Text("Search arenas by name or city...") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            FutsalDatePickerField(
                                onDateSelected = {
                                    onIntent(HomeIntent.DateChanged(it.toString()))
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        GenericSegmentedToggle(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            selectedOption = state.viewMode,
                            options = listOf(ViewMode.GRID, ViewMode.MAP),
                            onOptionSelected = { onIntent(HomeIntent.ViewModeChanged(it)) },
                            labelProvider = { if (it == ViewMode.GRID) "List" else "Map" },
                            iconProvider = { mode ->
                                if (mode == ViewMode.GRID) Icons.Default.GridView else Icons.Default.Map
                            }
                        )
                    }

                    item {
                        androidx.compose.animation.AnimatedContent(
                            targetState = state.isLocationEnabled,
                            transitionSpec = {
                                (fadeIn() + expandVertically()) togetherWith (fadeOut() + shrinkVertically())
                            },
                            label = "BannerTransition"
                        ) { locationEnabled ->
                            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                Column {
                                    Spacer(modifier = Modifier.height(24.dp))
                                    if (!locationEnabled) {
                                        LocationWarningBanner(onIntent = onIntent)
                                    } else {
                                        LocationSuccessBanner()
                                    }
                                }
                            }

                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        // Stats Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(
                                Modifier.weight(1f),
                                "34",
                                "Arenas",
                                BrandGreen,
                                LightGreenBG
                            )
                            StatCard(
                                Modifier.weight(1f), "24/7", "Booking", BrandGreen, LightGreenBG
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
                    }

                    when {
                        state.isLoading && state.arenaList.isNullOrEmpty() -> {
                            items(5, key = { "shimmer_$it" }) {
                                ArenaShimmerItem()
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                        !state.isLoading && state.arenaList?.isEmpty() == true -> {
                            item(key = "empty_state") {
                                EmptyStateComponent(
                                    onResetFilters = {
                                        onIntent(HomeIntent.SearchChanged(""))
                                    }
                                )
                            }
                        }

                        state.viewMode == ViewMode.MAP -> {
                            item(key = "map_view") {
                                ArenaMapView(
                                    arenas = state.arenaList ?: emptyList(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(500.dp)
                                        .padding(horizontal = 20.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }
                        }

                        else -> {
                            arenasListSection(
                                arenas = state.arenaList,
                                onItemClick = { arena ->
                                    // onIntent(HomeIntent.ArenaClicked(arena.id))
                                }
                            )
                        }
                    }

                }
            }

        }
    }
}

fun LazyListScope.arenasListSection(
    arenas: List<Arenas>?,
    onItemClick: (Arenas) -> Unit
) {
    if (arenas.isNullOrEmpty()) return

    item(key = "header_all_arenas") {
        Text(
            text = "All Arenas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        )
    }

    items(
        items = arenas,
        key = { arena -> arena.id!! }
    ) { arena ->
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .animateItem()
        ) {
            ArenaCard(
                arena = arena,
                onItemClick = { onItemClick(arena) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun ArenaMapView(
    arenas: List<Arenas>,
    modifier: Modifier = Modifier
) {
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(zoomControlsEnabled = false)
    ) {
        arenas.forEach { arena ->
            if (arena.latitude != null && arena.longitude != null) {
                Marker(
                    state = MarkerState(position = LatLng(arena.latitude, arena.longitude)),
                    title = arena.name,
                    snippet = arena.address,
                    onClick = {
                        // You can show a small card or navigate here
                        false
                    }
                )
            }
        }
    }
}


@Composable
fun StatCard(
    modifier: Modifier, value: String, label: String, textColor: Color, bgColor: Color
) {
    Surface(
        modifier = modifier, color = bgColor, shape = RoundedCornerShape(12.dp)
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
        state = HomeState(), onIntent = {})
}