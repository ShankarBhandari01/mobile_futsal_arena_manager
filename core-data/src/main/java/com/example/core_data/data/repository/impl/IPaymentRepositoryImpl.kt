package com.example.core_data.data.repository.impl

import com.example.core_domain.domain.apis.IPaymentApi
import com.example.core_domain.domain.dto.PaymentIntentResponseDTO
import com.example.core_domain.domain.dto.ReservationRequestDTO
import com.example.core_domain.domain.dto.ReservationResponseDTO
import com.example.core_domain.domain.repository.IPaymentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IPaymentRepositoryImpl @Inject constructor(
    private val api: IPaymentApi
) : IPaymentRepository {
    override suspend fun createPayment(bookingId: String): Result<PaymentIntentResponseDTO> {
        return api.createPayment(bookingId)
    }

    override suspend fun reserve(request: ReservationRequestDTO): Result<ReservationResponseDTO> {
        return api.reserve(request)
    }
}