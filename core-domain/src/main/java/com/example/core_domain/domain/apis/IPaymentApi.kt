package com.example.core_domain.domain.apis

import com.example.core_domain.domain.dto.PaymentIntentResponseDTO
import com.example.core_domain.domain.dto.ReservationRequestDTO
import com.example.core_domain.domain.dto.ReservationResponseDTO

interface IPaymentApi {
    suspend fun createPayment(bookingId: String): Result<PaymentIntentResponseDTO>
    suspend fun reserve(request: ReservationRequestDTO): Result<ReservationResponseDTO>
}

