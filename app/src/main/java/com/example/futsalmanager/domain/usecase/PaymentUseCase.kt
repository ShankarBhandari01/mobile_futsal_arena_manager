package com.example.futsalmanager.domain.usecase

import com.example.futsalmanager.data.remote.dto.ReservationRequestDTO
import com.example.futsalmanager.domain.repository.PaymentRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class PaymentUseCase @Inject constructor(
    private val repo: PaymentRepository
) {

    suspend fun createPayment(userId: String) {
        repo.createPayment(userId)
    }

    suspend fun reserve(request: ReservationRequestDTO) {
        repo.reserve(request)
    }
}