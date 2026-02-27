package com.example.core_domain.domain.usecase

import com.example.core_data.data.repository.IBookingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecurringBookingUseCase @Inject constructor(
    private val bookingRepository: IBookingRepository
) {


}