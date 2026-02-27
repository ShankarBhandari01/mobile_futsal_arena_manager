package com.example.core_domain.domain.usecase

import com.example.core_data.data.di.ApplicationScope
import com.example.core_data.data.model.LocationModel
import com.example.core_data.data.remote.dto.ArenaListResponse
import com.example.core_data.data.repository.IAuthRepository
import com.example.core_data.data.repository.IHomeRepository
import com.example.core_data.data.repository.ILocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val repo: IHomeRepository,
    private val location: ILocationRepository,
    @ApplicationScope private val appScope: CoroutineScope,
    private val authRepo: IAuthRepository
) {
    val userFlow get() = authRepo.getUser
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

    fun formatDateForApi(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateStr)
            date?.let { outputFormat.format(it) } ?: dateStr
        } catch (e: Exception) {
            dateStr // Fallback to original
        }
    }
}
