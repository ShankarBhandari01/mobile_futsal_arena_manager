package com.example.core_domain.domain.usecase

import com.example.core_domain.domain.dto.ReservationRequestDTO
import com.example.core_domain.domain.dto.ReserveWithPaymentIntent
import com.example.core_domain.domain.model.Slot
import com.example.core_domain.domain.repository.IAuthRepository
import com.example.core_domain.domain.repository.IBookingRepository
import com.example.core_domain.domain.repository.IPaymentRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingUseCase @Inject constructor(
    private val bookingRepository: IBookingRepository,
    private val paymentRepository: IPaymentRepository,
    private val authRepository: IAuthRepository
) {
    operator fun invoke(
        subDomain: String,
        courtId: String,
        date: String,
        includeStatus: Boolean
    ): Flow<Result<List<Slot>>> = flow {

        while (currentCoroutineContext().isActive) {
            val result = bookingRepository.getCourtSlots(
                subDomain = subDomain,
                courtId = courtId,
                date = date,
                includeStatus = includeStatus
            )

            emit(result)

            delay(10_000) // 10 sec polling
        }
    }

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

    suspend fun createPaymentIntent(
        reservationRequestDTO: ReservationRequestDTO,
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