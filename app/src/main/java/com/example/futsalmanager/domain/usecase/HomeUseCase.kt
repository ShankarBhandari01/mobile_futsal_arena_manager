package com.example.futsalmanager.domain.usecase

import android.location.Location
import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.domain.repository.HomeRepository
import com.example.futsalmanager.domain.repository.LocationRepository
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val repo: HomeRepository,
    location: LocationRepository
) {
    suspend fun getArenaList(
        search: String,
        offset: Int,
        limit: Int,
        date: String?
    ): Result<ArenaListResponse> {

        val sanitizedSearch = search.trim()

        // Don't call API if search is just 1 character
        if (sanitizedSearch.isNotEmpty() && sanitizedSearch.length < 2) {
            return Result.success(ArenaListResponse())
        }
        // If your DatePicker gives "Feb 05, 2026", convert it to "2026-02-05"
        val formattedDate = date?.let { formatDateForApi(it) } ?: ""
        return repo.getArenaList(
            search = sanitizedSearch,
            offset = offset,
            limit = limit,
            date = formattedDate
        )
    }

    private fun formatDateForApi(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateStr)
            date?.let { outputFormat.format(it) } ?: dateStr
        } catch (e: Exception) {
            dateStr // Fallback to original
        }
    }

    val userLocation = location.getLiveLocation()

    fun calculateDistance(
        userLat: Double, userLon: Double,
        arenaLat: Double, arenaLon: Double
    ): Float {
        val results = FloatArray(1)

        Location.distanceBetween(
            userLat,
            userLon,
            arenaLat,
            arenaLon, results
        )
        return results[0] / 1000 // Convert meters to Kilometers
    }

    val isLocationEnable = location.checkLocationStatus()
    val isGpsEnable = location.checkGpsStatus()
}