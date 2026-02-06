package com.example.futsalmanager.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import androidx.core.location.LocationManagerCompat
import com.example.futsalmanager.domain.model.LocationModel
import com.example.futsalmanager.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationServiceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationRepository {
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun checkGpsStatus(): Boolean {
        return try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            false
        }
    }

    override fun checkLocationStatus(): Boolean {
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    @SuppressLint("MissingPermission")
    override fun getLiveLocation(): Flow<LocationModel> = callbackFlow {

        //  Define the callback that will receive location updates
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let { location ->
                    // Map Android Location to your Domain LocationModel
                    trySend(
                        LocationModel(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                }
            }
        }

        //  Configure the location request
        val request = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY, // Battery Optimization
            5000L // Interval (5 seconds)
        ).apply {
            setMinUpdateIntervalMillis(2000L) // Fastest interval
        }.build()


        client.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            close(e) // Close the flow if the request fails
        }

        // Cleanup: When the flow is cancelled, remove the callback
        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }


}