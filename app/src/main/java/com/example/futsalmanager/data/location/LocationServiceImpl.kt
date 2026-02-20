package com.example.futsalmanager.data.location

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.location.LocationManagerCompat
import com.example.futsalmanager.domain.model.LocationModel
import com.example.futsalmanager.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationRepository {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override suspend fun checkGpsStatus(): Boolean = try {
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } catch (_: Exception) {
        false
    }

    override suspend fun checkLocationStatus(): Boolean =
        LocationManagerCompat.isLocationEnabled(locationManager)

    override fun observeLocationStatus(): Flow<Boolean> = callbackFlow {
        trySend(LocationManagerCompat.isLocationEnabled(locationManager))

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                    trySend(LocationManagerCompat.isLocationEnabled(locationManager))
                }
            }
        }

        context.registerReceiver(
            receiver,
            IntentFilter(
                LocationManager.PROVIDERS_CHANGED_ACTION
            )
        )

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }.flowOn(Dispatchers.IO)

    @SuppressLint("MissingPermission")
    override fun getLiveLocation(): Flow<LocationModel> = callbackFlow {

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val bestLocation = result.lastLocation ?: result.locations.firstOrNull()
                bestLocation?.let {
                    Log.d("LocationService", "Update: ${it.latitude}, ${it.longitude}")
                    trySend(LocationModel(it.latitude, it.longitude))
                }
            }
        }

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(2000L)
            .build()

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
            .addOnFailureListener { e ->
                close(e)
            }

        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }.flowOn(Dispatchers.IO)
}