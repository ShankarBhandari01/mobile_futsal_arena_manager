package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.PaymentIntentResponseDTO
import com.example.futsalmanager.data.remote.dto.ReservationRequestDTO
import com.example.futsalmanager.data.remote.dto.ReservationResponseDTO

interface PaymentApi {
    suspend fun createPayment(userId: String): Result<PaymentIntentResponseDTO>
    suspend fun reserve(request: ReservationRequestDTO): Result<ReservationResponseDTO>
}

