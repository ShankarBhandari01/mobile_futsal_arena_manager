package com.example.feature_home.home.booking

import com.example.core_domain.domain.dto.ReserveWithPaymentIntent


sealed interface BookingEffect {
    data class ShowError(val message: String) : BookingEffect
    data class ShowPaymentDialog(val paymentIntent: com.example.core_domain.domain.dto.ReserveWithPaymentIntent) : BookingEffect
}


