package com.example.futsalmanager.domain.usecase

import com.example.futsalmanager.domain.repository.BookingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecurringBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {


}