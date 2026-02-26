package com.example.futsalmanager.data.repository

import com.example.futsalmanager.data.remote.api.IPaymentApi
import com.example.futsalmanager.data.remote.dto.PaymentIntentResponseDTO
import com.example.futsalmanager.data.remote.dto.ReservationRequestDTO
import com.example.futsalmanager.data.remote.dto.ReservationResponseDTO
import com.example.futsalmanager.domain.repository.IPaymentRepository
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