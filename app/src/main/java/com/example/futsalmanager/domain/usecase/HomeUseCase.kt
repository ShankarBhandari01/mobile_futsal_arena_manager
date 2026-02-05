package com.example.futsalmanager.domain.usecase

import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.domain.repository.HomeRepository
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val repo: HomeRepository
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
}