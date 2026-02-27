package com.example.core_data.data.remote.api

import com.example.core_data.data.remote.dto.PaymentIntentResponseDTO
import com.example.core_data.data.remote.dto.ReservationRequestDTO
import com.example.core_data.data.remote.dto.ReservationResponseDTO

interface IPaymentApi {
    suspend fun createPayment(bookingId: String): Result<PaymentIntentResponseDTO>
    suspend fun reserve(request: ReservationRequestDTO): Result<ReservationResponseDTO>
}

