package com.example.feature_home.home.booking

import com.example.core_data.data.remote.dto.ReserveWithPaymentIntent


sealed interface BookingEffect {
    data class ShowError(val message: String) : BookingEffect
    data class ShowPaymentDialog(val paymentIntent: ReserveWithPaymentIntent) : BookingEffect
}


