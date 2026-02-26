package com.example.futsalmanager.ui.home

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.futsalmanager.core.ui.sharedComposables.ArenaCard
import com.example.futsalmanager.core.ui.sharedComposables.ArenaShimmerItem
import com.example.futsalmanager.core.ui.states.BannerState
import com.example.futsalmanager.core.ui.sharedComposables.EmptyStateComponent
import com.example.futsalmanager.core.ui.sharedComposables.FutsalDatePickerField
import com.example.futsalmanager.core.ui.sharedComposables.GenericSegmentedToggle
import com.example.futsalmanager.core.ui.sharedComposables.HomePermissionWrapper
import com.example.futsalmanager.core.ui.sharedComposables.LocationSuccessBanner
import com.example.futsalmanager.core.ui.sharedComposables.LocationWarningBanner
import com.example.futsalmanager.core.ui.sharedComposables.LogoutConfirmationDialog
import com.example.futsalmanager.core.ui.sharedComposables.TopMessageBanner
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.User
import com.example.futsalmanager.ui.component.ArenaMapView
import com.example.futsalmanager.ui.component.StatCard
import com.example.futsalmanager.ui.home.navigation_drawer.FutsalDrawerSheet
import com.example.futsalmanager.ui.home.viewModels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FutsalHomeScreenRoute(
    onLogout: () -> Unit,
    arenaClicked: (String) -> Unit
) {
    var bannerState by remember { mutableStateOf<BannerState>(BannerState.Hidden) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val viewmodel = hiltViewModel<HomeViewModel>()
    val context = LocalContext.current
    val user by viewmodel.user.collectAsStateWithLifecycle(
        lifecycleOwner = lifecycleOwner
    )
    val state by viewmodel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifecycleOwner
    )

    LaunchedEffect(bannerState) {
        if (bannerState !is BannerState.Hidden) {
            delay(4000)
            bannerState = BannerState.Hidden
        }
    }

    DisposableEffect(Unit) {
        viewmodel.dispatch(HomeIntent.ScreenStarted)
        onDispose {
            viewmodel.dispatch(HomeIntent.ScreenStopped)
        }
    }

    LaunchedEffect(Unit) {
        viewmodel.effect.collect { e ->
            when (e) {
                is HomeEffect.ShowError -> {
                    bannerState = BannerState.Error(e.message)
                }

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
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                }

                is HomeEffect.NavigateToBookingWithArea -> {
                    arenaClicked(e.arena.id)
                }
            }
        }
    }

    HomePermissionWrapper(
        onPermissionChanged = { isGranted ->
            if (isGranted) {
                viewmodel.dispatch(HomeIntent.OnPermissionsGranted)
            }
        }
    ) {

        FutsalHomeScreen(
            user = user,
            state = state,
            onIntent = viewmodel::dispatch
        )

        TopMessageBanner(
            state = bannerState,
            onDismiss = { bannerState = BannerState.Hidden }
        )
    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FutsalHomeScreen(
    user: User? = null,
    state: HomeState = HomeState(),
    onIntent: (HomeIntent) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val pullToRefreshState = rememberPullToRefreshState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastItem != null && lastItem.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !state.isLoading && state.arenaList?.isNotEmpty() == true) {
            onIntent(HomeIntent.LoadNextPage)
        }
    }

    if (state.showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                onIntent(HomeIntent.ConfirmLogout)
            },
            onDismiss = {
                haptics.performHapticFeedback(HapticFeedbackType.Reject)
                onIntent(HomeIntent.DismissLogoutDialog)
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            FutsalDrawerSheet(
                user = user,
                drawerState, scope,
                onIntent
            )
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
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        "F",
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
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
                            Icon(
                                Icons.Default.Menu,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = "Menu"
                            )
                        }
                    }, actions = {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                val first = user?.firstName?.firstOrNull()?.uppercase()
                                val last = user?.lastName?.firstOrNull()?.uppercase()

                                Text(
                                    "$first$last",
                                    color = MaterialTheme.colorScheme.onErrorContainer,
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
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp)
                    ) {
                        PullToRefreshDefaults.LoadingIndicator(
                            state = pullToRefreshState,
                            isRefreshing = state.isLoading,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            ) {
                LazyColumn(
                    state = listState,
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
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(100.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents, // Use a trophy icon
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
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
                        AnimatedContent(
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
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                            StatCard(
                                Modifier.weight(1f), "24/7",
                                "Booking",
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                            StatCard(
                                Modifier.weight(1f),
                                "Instant",
                                "Confirm",
                                MaterialTheme.colorScheme.onTertiaryContainer,
                                MaterialTheme.colorScheme.tertiaryContainer
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
                                    onIntent,
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
                                    onIntent(HomeIntent.ArenaClicked(arena))
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
        key = { arena -> arena.id }
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


@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun FutsalHomeScreenPreview() {
    FutsalHomeScreen(
        state = HomeState(), onIntent = {})
}