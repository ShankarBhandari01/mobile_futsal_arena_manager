package com.example.futsalmanager.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.ui.home.HomeIntent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArenaMapView(
    onIntent: (HomeIntent) -> Unit,
    arenas: List<Arenas>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    var searchText by rememberSaveable { mutableStateOf("") }
    var maxDistanceKm by remember { mutableFloatStateOf(20f) }

    var selectedArena by remember { mutableStateOf<Arenas?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    val mapRef = remember { mutableStateOf<MapView?>(null) }

    val locationProvider = remember { GpsMyLocationProvider(context) }
    var locationOverlay by remember { mutableStateOf<MyLocationNewOverlay?>(null) }

    Box(modifier.fillMaxSize()) {

        /* ---------------- MAP ---------------- */

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                MapView(ctx).apply {

                    mapRef.value = this

                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    val nepalBounds = BoundingBox(30.4, 88.2, 26.3, 80.0)
                    setScrollableAreaLimitDouble(nepalBounds)
                    zoomToBoundingBox(nepalBounds, false)

                    controller.setZoom(10.0)

                    setOnTouchListener { v, _ ->
                        v.parent.requestDisallowInterceptTouchEvent(true)
                        false
                    }

                    val overlay = MyLocationNewOverlay(locationProvider, this).apply {
                        enableMyLocation()
                    }

                    locationOverlay = overlay
                    overlays.add(overlay)
                }
            },
            update = {} // IMPORTANT: no redraw here
        )


        /* ---------------- MARKERS UPDATE ---------------- */

        LaunchedEffect(arenas, searchText, maxDistanceKm, locationOverlay) {
            mapRef.value?.let { map ->
                updateMarkers(
                    mapView = map,
                    arenas = arenas,
                    maxDist = maxDistanceKm,
                    searchText = searchText,
                    userLoc = locationOverlay
                ) { arena ->
                    selectedArena = arena
                    showSheet = true
                }
            }
        }


        /* ---------------- FLOATING FILTER CARD ---------------- */

        Card(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .width(260.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            )
        ) {
            Column(Modifier.padding(12.dp)) {

                Text("Filters", fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search arenas...") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Search, null)
                    }
                )

                Spacer(Modifier.height(12.dp))

                Text("Max distance: ${maxDistanceKm.toInt()} km")

                Slider(
                    value = maxDistanceKm,
                    onValueChange = { maxDistanceKm = it },
                    valueRange = 1f..100f
                )
            }
        }


        /* ---------------- LOCATION BUTTON ---------------- */

        FloatingActionButton(
            onClick = { locationOverlay?.enableFollowLocation() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color.White
        ) {
            Icon(Icons.Default.MyLocation, null, tint = Color.Blue)
        }


        /* ---------------- BOTTOM SHEET ---------------- */

        if (showSheet && selectedArena != null) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showSheet = false }
            ) {
                ArenaDetailSheet(
                    arena = selectedArena!!,
                    userLocation = locationOverlay?.myLocation,
                    onBookClick = {
                        onIntent(HomeIntent.ArenaClicked(selectedArena!!))
                        showSheet = false
                    }
                )
            }
        }
    }
}


/* ------------------------------------------------ */
/* --------------- MARKER LOGIC ------------------- */
/* ------------------------------------------------ */

private fun updateMarkers(
    mapView: MapView,
    arenas: List<Arenas>,
    maxDist: Float,
    searchText: String,
    userLoc: MyLocationNewOverlay?,
    onSelect: (Arenas) -> Unit
) {
    val oldMarkers = mapView.overlays.filterIsInstance<Marker>()
    mapView.overlays.removeAll(oldMarkers)

    val userPoint = userLoc?.myLocation

    arenas.forEach { arena ->

        val name = arena.name ?: return@forEach

        if (searchText.isNotBlank() &&
            !name.contains(searchText, true)
        ) return@forEach

        val point = GeoPoint(arena.latitude ?: 0.0, arena.longitude ?: 0.0)

        val distKm = userPoint?.distanceToAsDouble(point)?.div(1000)

        if (userPoint != null && distKm != null && distKm > maxDist)
            return@forEach

        val marker = Marker(mapView).apply {
            position = point
            title = name

            setOnMarkerClickListener { _, _ ->
                onSelect(arena)
                true
            }
        }

        mapView.overlays.add(marker)
    }

    mapView.invalidate()
}
