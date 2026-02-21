package com.example.futsalmanager.domain.usecase

import com.example.futsalmanager.domain.model.Slot
import com.example.futsalmanager.domain.repository.BookingRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    fun arenaById(id: String) = bookingRepository.getArenaWithCourts(id)

    suspend fun refreshArenaDetailsWithCourts(id: String) {
        bookingRepository.getArenaById(id)
            .filterNotNull()
            .first()
            .subdomain
            ?.let {
                bookingRepository.arenasSubDomainCourts(it)
            }
    }

    suspend fun getCourtSlots(
        subDomain: String,
        courtId: String,
        date: String,
        includeStatus: Boolean
    ): Result<List<Slot>> {
        return bookingRepository.getCourtSlots(subDomain, courtId, date, includeStatus)
    }
}