package com.example.futsalmanager.domain.usecase

import com.example.futsalmanager.core.utils.ApplicationScope
import com.example.futsalmanager.core.utils.Common.formatDateForApi
import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.domain.model.LocationModel
import com.example.futsalmanager.domain.repository.HomeRepository
import com.example.futsalmanager.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val location: LocationRepository,
    @ApplicationScope private val appScope: CoroutineScope,
) {
    val arenas = repo.getArenaListFromDB()

    val userLocation = location.getLiveLocation().stateIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val observerLocationStatus = location.observeLocationStatus().stateIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    suspend fun isLocationEnabled() = location.checkLocationStatus()

    suspend fun isGpsEnabled() = location.checkGpsStatus()

    suspend fun getArenaListFromApi(
        search: String,
        offset: Int,
        limit: Int,
        date: String?,
        location: LocationModel? = null,
        isGpsEnabled: Boolean,
    ): Result<ArenaListResponse> {
        val sanitizedSearch = search.trim()

        if (sanitizedSearch.isNotEmpty() && sanitizedSearch.length < 2) {
            return Result.success(ArenaListResponse())
        }

        val formattedDate = date?.let { formatDateForApi(it) } ?: ""

        val shouldSortByDistance = isGpsEnabled &&
                location?.latitude != 0.0 &&
                location?.longitude != 0.0

        return repo.getArenaListFromApi(
            search = sanitizedSearch,
            offset = offset,
            limit = limit,
            date = formattedDate,
            lat = if (shouldSortByDistance) location?.latitude else null,
            lng = if (shouldSortByDistance) location?.longitude else null,
        )
    }
}