package com.example.futsalmanager.domain.usecase

import com.example.futsalmanager.core.utils.Common.formatDateForApi
import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.domain.model.LocationModel
import com.example.futsalmanager.domain.model.Slot
import com.example.futsalmanager.domain.repository.HomeRepository
import com.example.futsalmanager.domain.repository.LocationRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val location: LocationRepository
) {
    val userLocation = location.getLiveLocation()

    suspend fun isLocationEnable() = location.checkLocationStatus()

    suspend fun isGpsEnable() = location.checkGpsStatus()

    val observerLocationStatus = location.observeLocationStatus()

    suspend fun getArenaListFromApi(
        search: String,
        offset: Int,
        limit: Int,
        date: String?,
        location: LocationModel? = null,
        isGpsEnabled: Boolean,
    ): Result<ArenaListResponse> {

        val sanitizedSearch = search.trim()

        // Don't call API if search is just 1 character
        if (sanitizedSearch.isNotEmpty() && sanitizedSearch.length < 2) {
            return Result.success(ArenaListResponse())
        }
        // If DatePicker gives "Feb 05, 2026", convert it to "2026-02-05"
        val formattedDate = date?.let { formatDateForApi(it) } ?: ""

        // check if we have valid user location
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

    val arenas = repo.getArenaListFromDB()

    fun arenaById(id: String) = repo.getArenaWithCourts(id)

    suspend fun refreshArenaDetailsWithCourts(id: String) {
        repo.getArenaById(id)
            .filterNotNull()
            .first()
            .subdomain
            ?.let {
                repo.arenasSubDomainCourts(it)
            }
    }

    suspend fun getCourtSlots(
        subDomain: String,
        courtId: String,
        date: String,
        includeStatus: Boolean
    ):Result<List<Slot>> {
        return repo.getCourtSlots(subDomain, courtId, date, includeStatus)
    }
}