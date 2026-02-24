package com.example.futsalmanager.domain.usecase

import com.example.futsalmanager.data.remote.dto.ReservationRequestDTO
import com.example.futsalmanager.data.remote.dto.ReserveWithPaymentIntent
import com.example.futsalmanager.domain.model.Slot
import com.example.futsalmanager.domain.repository.AuthRepository
import com.example.futsalmanager.domain.repository.BookingRepository
import com.example.futsalmanager.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val paymentRepository: PaymentRepository,
    private val authRepository: AuthRepository
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

    suspend fun createPaymentIntent(
        reservationRequestDTO: ReservationRequestDTO
    ): Result<ReserveWithPaymentIntent> {
        // get user
        val user = authRepository.getUser.first()

        return paymentRepository.reserve(reservationRequestDTO).fold(
            onSuccess = { reservation ->
                paymentRepository.createPayment(reservation.booking.id!!).map { paymentIntent ->
                    ReserveWithPaymentIntent(
                        paymentIntentResponseDTO = paymentIntent,
                        reservationResponseDTO = reservation,
                        user = user
                    )
                }
            },
            onFailure = { Result.failure(it) }
        )
    }

}