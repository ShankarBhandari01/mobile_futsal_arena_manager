package com.example.futsalmanager.data.repository

import com.example.futsalmanager.data.remote.api.PaymentApi
import com.example.futsalmanager.data.remote.dto.PaymentIntentResponseDTO
import com.example.futsalmanager.data.remote.dto.ReservationRequestDTO
import com.example.futsalmanager.data.remote.dto.ReservationResponseDTO
import com.example.futsalmanager.domain.repository.PaymentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRepository {
    override suspend fun createPayment(bookingId: String): Result<PaymentIntentResponseDTO> {
        return api.createPayment(bookingId)
    }

    override suspend fun reserve(request: ReservationRequestDTO): Result<ReservationResponseDTO> {
        return api.reserve(request)
    }
}